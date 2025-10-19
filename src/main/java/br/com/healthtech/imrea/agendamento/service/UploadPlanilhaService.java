package br.com.healthtech.imrea.agendamento.service;

import br.com.healthtech.imrea.agendamento.domain.Consulta;
import br.com.healthtech.imrea.agendamento.domain.Profissional;
import br.com.healthtech.imrea.agendamento.domain.RegistroAgendamento;
import br.com.healthtech.imrea.agendamento.domain.UploadLog;
import br.com.healthtech.imrea.interacao.domain.TipoInteracao;
import br.com.healthtech.imrea.interacao.service.InteracaoAutomatizadaService;
import br.com.healthtech.imrea.paciente.domain.Cuidador;
import br.com.healthtech.imrea.paciente.domain.Paciente;
import br.com.healthtech.imrea.paciente.service.CuidadorService;
import br.com.healthtech.imrea.paciente.service.PacienteService;
import br.com.healthtech.imrea.usuario.service.UsuarioService;
import com.alibaba.excel.EasyExcel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class UploadPlanilhaService {

    private static final Logger logger = LoggerFactory.getLogger(UploadPlanilhaService.class);

    @Inject
    PacienteService pacienteService;

    @Inject
    ConsultaService consultaService;

    @Inject
    ProfissionalService profissionalService;

    @Inject
    UsuarioService usuarioService;

    @Inject
    CuidadorService cuidadorService;

    List<Consulta> listaConsultas = new java.util.ArrayList<>();

    @Inject
    InteracaoAutomatizadaService interacaoAutomatizadaService;

    @Transactional
    public List<RegistroAgendamento> processarPlanilha(FileUpload fileUpload) {

        String nomeArquivo = fileUpload.fileName();
        logger.info("Processando planilha do arquivo {}", nomeArquivo);

        try {
            if (nomeArquivo.toLowerCase().endsWith(".xlsx")) {
                List<RegistroAgendamento> listasAgendamentos = EasyExcel.read(fileUpload.uploadedFile().toFile())
                        .head(RegistroAgendamento.class)
                        .sheet()
                        .doReadSync();

                return listasAgendamentos.stream()
                        .map(this::normalizarCamposNulos)
                        .collect(Collectors.toList());

            } else {
                logger.info("Formato de arquivo inválido");
            }

        } catch (Exception e) {
            logger.error("Erro ao processar planilha");
            logger.error(e.getMessage());
        }

        return Collections.emptyList();
    }

    private RegistroAgendamento normalizarCamposNulos(RegistroAgendamento registro) {
        // Itera sobre todos os campos (fields) declarados na classe RegistroAgendamento
        for (Field field : RegistroAgendamento.class.getDeclaredFields()) {
            // Verifica se o campo é do tipo String
            if (field.getType().equals(String.class)) {
                try {
                    // Torna o campo acessível (necessário para campos privados)
                    field.setAccessible(true);

                    // Pega o valor atual do campo no objeto 'registro'
                    String valorAtual = (String) field.get(registro);

                    // Se o valor for nulo, atribui a string vazia ""
                    if (valorAtual == null) {
                        field.set(registro, "");
                    }
                } catch (IllegalAccessException e) {
                    logger.error("Erro ao acessar campo por reflexão: " + field.getName(), e);
                    // Pode-se optar por re-lançar a exceção ou apenas logar
                }
            }
        }
        return registro;
    }

    @Transactional
    public void salvarDadosAgendamento(List<RegistroAgendamento> listasAgendamentos) {

        UploadLog uploadLog = new UploadLog();
        uploadLog.dataHoraUpload = new Date();
        uploadLog.statusUpload = "EM_PROCESSAMENTO";
        uploadLog.usuario = usuarioService.buscarUsuarioTeste();
        uploadLog.persist();

        StringBuilder detalhesErrosBuilder = new StringBuilder();

        /* REMOVER ESSA LINHA AQUI DEPOIS */
        listaConsultas = new java.util.ArrayList<>();
        for (RegistroAgendamento registro : listasAgendamentos) {
            try {
                processarUmRegistroComTransacao(registro, uploadLog);
                uploadLog.numRegistrosProcessados++;
            } catch (Exception e) {
                logger.error("Erro ao processar linha: " + e.getMessage(), e);
                uploadLog.numRegistrosComErro++;
                detalhesErrosBuilder.append("Erro na linha do paciente ")
                        .append(registro.getNomePaciente())
                        .append(": ")
                        .append(e.getMessage())
                        .append("; ");
            }
        }

        /* REMOVER ESSA LINHA AQUI DEPOIS */
        interacaoAutomatizadaService.enviarLembrete(listaConsultas, TipoInteracao.LEMBRETE_24H);

        if (uploadLog.numRegistrosComErro > 0) {
            uploadLog.statusUpload = "FINALIZADO COM ERROS";
            uploadLog.detalhesErros = detalhesErrosBuilder.toString();
        } else {
            uploadLog.statusUpload = "SUCESSO";
        }

        uploadLog.persist();
    }

    @Transactional
    public void processarUmRegistroComTransacao(RegistroAgendamento registro, UploadLog uploadLog) {
        try {

            Paciente paciente = new Paciente(
                    registro.getNomePaciente(),
                    registro.getNumeroPaciente(),
                    new SimpleDateFormat("dd/MM/yyyy").parse(registro.getDataNascimentoPaciente())
            );

            Cuidador cuidador = new Cuidador(registro.getNomeAcompanhante(), registro.getNumeroAcompanhante());
            paciente.cuidadores.add(cuidadorService.buscarOuCriarCuidador(cuidador));

            paciente = pacienteService.buscarOuCriarPaciente(paciente);

            Profissional profissional = new Profissional(registro.getNomeMedico(), registro.getEspecialidade());
            profissional = profissionalService.buscarOuCriarMedico(profissional);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime dataAgenda = LocalDateTime.parse(registro.getDataAgendamento() + " " + registro.getHoraAgendamento(), formatter);

            Consulta consulta = new Consulta(
                    paciente,
                    profissional,
                    uploadLog,
                    dataAgenda,
                    registro.getLinkConsulta(),
                    registro.getCodigoConsulta(),
                    registro.getObsAgendamento()
            );

            consultaService.buscarOuCriarConsulta(consulta);
           listaConsultas.add(consulta);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Erro ao converter a data de nascimento do paciente.", e);
        }
    }
}