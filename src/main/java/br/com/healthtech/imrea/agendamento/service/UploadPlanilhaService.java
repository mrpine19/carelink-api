package br.com.healthtech.imrea.agendamento.service;

import br.com.healthtech.imrea.agendamento.domain.Consulta;
import br.com.healthtech.imrea.agendamento.domain.Profissional;
import br.com.healthtech.imrea.agendamento.domain.RegistroAgendamento;
import br.com.healthtech.imrea.agendamento.domain.UploadLog;
import br.com.healthtech.imrea.paciente.domain.Cuidador;
import br.com.healthtech.imrea.paciente.domain.Paciente;
import br.com.healthtech.imrea.paciente.service.CuidadorService;
import br.com.healthtech.imrea.paciente.service.PacienteService;
import br.com.healthtech.imrea.usuario.service.UsuarioService;
import com.alibaba.excel.EasyExcel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class UploadPlanilhaService {

    private static final Logger logger = LoggerFactory.getLogger(UploadPlanilhaService.class);

    private final PacienteService pacienteService;
    private final ConsultaService consultaService;
    private final ProfissionalService profissionalService;
    private final UsuarioService usuarioService;
    private final CuidadorService cuidadorService;

    public UploadPlanilhaService(PacienteService pacienteService, ConsultaService consultaService, ProfissionalService profissionalService, UsuarioService usuarioService, CuidadorService cuidadorService) {
        this.pacienteService = pacienteService;
        this.consultaService = consultaService;
        this.profissionalService = profissionalService;
        this.usuarioService = usuarioService;
        this.cuidadorService = cuidadorService;
    }


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
                return listasAgendamentos;
            } else {
                logger.info("Formato de arquivo inválido");
            }

        } catch (Exception e) {
            logger.error("Erro ao processar planilha");
            logger.error(e.getMessage());
        }

        return Collections.emptyList();
    }

    @Transactional
    public void salvarDadosAgendamento(List<RegistroAgendamento> listasAgendamentos) {

        UploadLog uploadLog = new UploadLog();
        uploadLog.dataHoraUpload = new Date();
        uploadLog.statusUpload = "EM_PROCESSAMENTO";
        uploadLog.usuario = usuarioService.buscarUsuarioTeste();
        uploadLog.persist();

        StringBuilder detalhesErrosBuilder = new StringBuilder();

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
            Date dataNascimentoPaciente = converterStringDeData(registro.getDataNascimentoPaciente());

            Paciente paciente = new Paciente(
                    registro.getNomePaciente(),
                    registro.getNumeroPaciente(),
                    dataNascimentoPaciente
            );

            Cuidador cuidador = new Cuidador(registro.getNomeAcompanhante(), registro.getNumeroAcompanhante());
            paciente.cuidadores.add(cuidadorService.buscarOuCriarCuidador(cuidador));

            paciente = pacienteService.buscarOuCriarPaciente(paciente);

            Profissional profissional = new Profissional(registro.getNomeMedico(), registro.getEspecialidade());
            profissional = profissionalService.buscarOuCriarMedico(profissional);

            Date dataAgenda = converterDataHora(registro.getDataAgendamento(), registro.getHoraAgendamento());
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
        } catch (ParseException e) {
            throw new IllegalArgumentException("Erro ao converter a data de nascimento do paciente.", e);
        }
    }

    private Date converterStringDeData(String dataString) throws ParseException {
        SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy/MM/dd");
        Date data = formatoEntrada.parse(dataString);
        return data;
    }

    private Date converterDataHora(String data, String hora) {
        try {
            // Converte a data do formato yyyy/MM/dd para dd/MM/yyyy antes de concatenar
            SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy/MM/dd");
            SimpleDateFormat formatoSaida = new SimpleDateFormat("dd/MM/yyyy");
            Date dataConvertida = formatoEntrada.parse(data);
            String dataFormatada = formatoSaida.format(dataConvertida);

            String dataHoraCompleta = dataFormatada + " " + hora;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy H:mm");
            LocalDateTime localDateTime = LocalDateTime.parse(dataHoraCompleta.trim(), formatter);

            return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao converter data e hora. Formato esperado: dd/MM/yyyy H:mm", e);
        }
    }
}