package br.com.healthtech.imrea.agendamento.service;

import br.com.healthtech.imrea.agendamento.domain.Agendamento;
import br.com.healthtech.imrea.agendamento.domain.RegistroAgendamento;
import br.com.healthtech.imrea.paciente.domain.Paciente;
import br.com.healthtech.imrea.paciente.service.PacienteService;
import com.alibaba.excel.EasyExcel;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

@ApplicationScoped
public class UploadPlanilhaService {

    private static final Logger logger = LoggerFactory.getLogger(UploadPlanilhaService.class);

    private final PacienteService pacienteService;
    private final AgendamentoService agendamentoService;

    public UploadPlanilhaService(PacienteService pacienteService, AgendamentoService agendamentoService) {
        this.pacienteService = pacienteService;
        this.agendamentoService = agendamentoService;
    }

    public void processarPlanilha(FileUpload fileUpload) {

        String nomeArquivo = fileUpload.fileName();
        logger.info("Processando planilha do arquivo {}", nomeArquivo);
        try {
            if (nomeArquivo.toLowerCase().endsWith(".xlsx")) {
                processarXlsx(fileUpload.uploadedFile().toFile());
            }

        } catch (Exception e) {
            logger.error("Erro ao processar planilha");
            logger.error(e.getMessage());
        }
    }

    public void processarXlsx(File planilha)  {
        List<RegistroAgendamento> listasAgendamentos;
        try {
            listasAgendamentos = EasyExcel.read(planilha)
                    .head(RegistroAgendamento.class)
                    .sheet()
                    .doReadSync();
            for (RegistroAgendamento registroAgendamento : listasAgendamentos) {
                Paciente paciente = new Paciente(registroAgendamento.getNomePaciente(), registroAgendamento.getNumeroPaciente());
                pacienteService.save(paciente);

                Agendamento agendamento = new Agendamento(paciente, registroAgendamento.getDataAgendamento(), registroAgendamento.getHoraAgendamento(),
                                                        registroAgendamento.getLinkConsulta(), registroAgendamento.getCodigoConsulta(), registroAgendamento.getObsAgendamento());
                agendamentoService.save(agendamento);
            }

        } catch (Exception e) {
            logger.error("Erro ao processar a planilha: " + e.getMessage(), e);
        }
    }

}
