package br.com.healthtech.imrea.agendamento.service;

import br.com.healthtech.imrea.agendamento.domain.Consulta;
import br.com.healthtech.imrea.agendamento.domain.Profissional;
import br.com.healthtech.imrea.agendamento.domain.RegistroAgendamento;
import br.com.healthtech.imrea.agendamento.domain.UploadLog;
import br.com.healthtech.imrea.agendamento.dto.UploadDTO;
import br.com.healthtech.imrea.paciente.domain.Paciente;
import br.com.healthtech.imrea.paciente.service.PacienteService;
import br.com.healthtech.imrea.usuario.service.UsuarioService;
import com.alibaba.excel.EasyExcel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class UploadPlanilhaService {

    private static final Logger logger = LoggerFactory.getLogger(UploadPlanilhaService.class);

    private final PacienteService pacienteService;
    private final ConsultaService consultaService;
    private final ProfissionalService profissionalService;
    private final UsuarioService usuarioService;

    public UploadPlanilhaService(PacienteService pacienteService, ConsultaService consultaService, ProfissionalService profissionalService, UsuarioService usuarioService) {
        this.pacienteService = pacienteService;
        this.consultaService = consultaService;
        this.profissionalService = profissionalService;
        this.usuarioService = usuarioService;
    }

    @Transactional
    public UploadDTO processarPlanilha(FileUpload fileUpload) {

        UploadLog uploadLog = new UploadLog();
        uploadLog.dataHoraUpload = new Date();
        uploadLog.nomeArquivo = fileUpload.fileName();
        uploadLog.statusUpload = "EM_PROCESSAMENTO";
        uploadLog.usuario = usuarioService.buscarUsuarioTeste();
        uploadLog.persist();

        String nomeArquivo = fileUpload.fileName();
        logger.info("Processando planilha do arquivo {}", nomeArquivo);

        try {
            if (nomeArquivo.toLowerCase().endsWith(".xlsx")) {
                processarXlsx(fileUpload.uploadedFile().toFile(), uploadLog);
            } else {
                uploadLog.statusUpload = "FALHA - FORMATO INVALIDO";
                uploadLog.numRegistrosComErro = 1;
            }

        } catch (Exception e) {
            logger.error("Erro ao processar planilha");
            logger.error(e.getMessage());

            uploadLog.statusUpload = "FALHA - ERRO INTERNO";
            uploadLog.detalhesErros = e.getMessage();
        }

        return new UploadDTO(uploadLog.statusUpload, uploadLog.numRegistrosProcessados, uploadLog.numRegistrosComErro);
    }

    public void processarXlsx(File planilha, UploadLog uploadLog)  {
        List<RegistroAgendamento> listasAgendamentos = EasyExcel.read(planilha)
                                                        .head(RegistroAgendamento.class)
                                                        .sheet()
                                                        .doReadSync();

        for (RegistroAgendamento registroAgendamento : listasAgendamentos) {
            try {
                Paciente paciente = new Paciente(registroAgendamento.getNomePaciente(), registroAgendamento.getNumeroPaciente());
                paciente = pacienteService.buscarOuCriarPaciente(paciente);

                Profissional profissional = new Profissional(registroAgendamento.getNomeMedico(), registroAgendamento.getEspecialidade());
                profissional = profissionalService.buscarOuCriarMedico(profissional);

                Consulta consulta = new Consulta(paciente, profissional, registroAgendamento.getDataAgendamento(), registroAgendamento.getHoraAgendamento(),
                        registroAgendamento.getLinkConsulta(), registroAgendamento.getCodigoConsulta(), registroAgendamento.getObsAgendamento());
                consultaService.buscarOuCriarConsulta(consulta);

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
