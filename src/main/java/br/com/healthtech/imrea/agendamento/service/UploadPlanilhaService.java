package br.com.healthtech.imrea.agendamento.service;

import br.com.healthtech.imrea.agendamento.domain.Consulta;
import br.com.healthtech.imrea.agendamento.domain.Profissional;
import br.com.healthtech.imrea.agendamento.domain.RegistroAgendamento;
import br.com.healthtech.imrea.agendamento.domain.UploadLog;
import br.com.healthtech.imrea.agendamento.dto.UploadDTO;
import br.com.healthtech.imrea.paciente.domain.InteracaoAutomatizada;
import br.com.healthtech.imrea.paciente.domain.Paciente;
import br.com.healthtech.imrea.paciente.service.InteracaoAutomatizadaService;
import br.com.healthtech.imrea.paciente.service.PacienteService;
import br.com.healthtech.imrea.usuario.service.UsuarioService;
import com.alibaba.excel.EasyExcel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
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
    private final InteracaoAutomatizadaService interacaoService;

    public UploadPlanilhaService(PacienteService pacienteService, ConsultaService consultaService, ProfissionalService profissionalService, UsuarioService usuarioService, InteracaoAutomatizadaService interacaoService) {
        this.pacienteService = pacienteService;
        this.consultaService = consultaService;
        this.profissionalService = profissionalService;
        this.usuarioService = usuarioService;
        this.interacaoService = interacaoService;
    }

    @Transactional
    public List<RegistroAgendamento> processarPlanilha(FileUpload fileUpload) { // <-- MUDANÇA 1: Retornar a lista

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
    public void salvarDadosAgendamento(List<RegistroAgendamento> listasAgendamentos)  {

        UploadLog uploadLog = new UploadLog();
        uploadLog.dataHoraUpload = new Date();
        uploadLog.statusUpload = "EM_PROCESSAMENTO";
        uploadLog.usuario = usuarioService.buscarUsuarioTeste();
        uploadLog.persist();

        for (RegistroAgendamento registroAgendamento : listasAgendamentos) {
            try {
                Paciente paciente = new Paciente(registroAgendamento.getNomePaciente(), registroAgendamento.getNumeroPaciente());
                paciente = pacienteService.buscarOuCriarPaciente(paciente);

                Profissional profissional = new Profissional(registroAgendamento.getNomeMedico(), registroAgendamento.getEspecialidade());
                profissional = profissionalService.buscarOuCriarMedico(profissional);

                String dataHoraCompleta = registroAgendamento.getDataAgendamento() + " " + registroAgendamento.getHoraAgendamento();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                LocalDateTime localDateTime = LocalDateTime.parse(dataHoraCompleta.trim(), formatter);
                Date dataAgenda = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

                Consulta consulta = new Consulta(paciente, profissional, uploadLog, dataAgenda,
                        registroAgendamento.getLinkConsulta(), registroAgendamento.getCodigoConsulta(), registroAgendamento.getObsAgendamento());
                consultaService.buscarOuCriarConsulta(consulta);

                interacaoService.buscarOuCriarInteracao(consulta);

                uploadLog.numRegistrosProcessados++;

            } catch (Exception e) {
                logger.error("Erro ao processar a planilha: " + e.getMessage(), e);

                uploadLog.numRegistrosComErro++;
                uploadLog.detalhesErros += "Erro na linha do paciente " + registroAgendamento.getNomePaciente() + ": " + e.getMessage() + "; ";
            }
        }

        if (uploadLog.numRegistrosComErro > 0) {
            uploadLog.statusUpload = "FINALIZADO COM ERROS";
        } else {
            uploadLog.statusUpload = "SUCESSO";
        }
    }
}
