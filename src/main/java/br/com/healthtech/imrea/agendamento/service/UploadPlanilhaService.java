package br.com.healthtech.imrea.agendamento.service;

import br.com.healthtech.imrea.agendamento.domain.Consulta;
import br.com.healthtech.imrea.agendamento.domain.Profissional;
import br.com.healthtech.imrea.agendamento.domain.RegistroAgendamento;
import br.com.healthtech.imrea.paciente.domain.Paciente;
import br.com.healthtech.imrea.paciente.service.PacienteService;
import com.alibaba.excel.EasyExcel;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class UploadPlanilhaService {

    private static final Logger logger = LoggerFactory.getLogger(UploadPlanilhaService.class);

    private final PacienteService pacienteService;
    private final ConsultaService consultaService;
    private final ProfissionalService profissionalService;

    public UploadPlanilhaService(PacienteService pacienteService, ConsultaService consultaService, ProfissionalService profissionalService) {
        this.pacienteService = pacienteService;
        this.consultaService = consultaService;
        this.profissionalService = profissionalService;
    }

    public List<RegistroAgendamento> processarPlanilha(FileUpload fileUpload) {

        String nomeArquivo = fileUpload.fileName();
        logger.info("Processando planilha do arquivo {}", nomeArquivo);

        List<RegistroAgendamento> listasAgendamentos = new ArrayList<>();
        try {
            if (nomeArquivo.toLowerCase().endsWith(".xlsx")) {
                listasAgendamentos = processarXlsx(fileUpload.uploadedFile().toFile());
            }

        } catch (Exception e) {
            logger.error("Erro ao processar planilha");
            logger.error(e.getMessage());
        }

        return listasAgendamentos;
    }

    public List<RegistroAgendamento> processarXlsx(File planilha)  {
        List<RegistroAgendamento> listasAgendamentos = new ArrayList<>();
        try {
            listasAgendamentos = EasyExcel.read(planilha)
                    .head(RegistroAgendamento.class)
                    .sheet()
                    .doReadSync();

            for (RegistroAgendamento registroAgendamento : listasAgendamentos) {
                Paciente paciente = new Paciente(registroAgendamento.getNomePaciente(), registroAgendamento.getNumeroPaciente());
                paciente = pacienteService.buscarOuCriarPaciente(paciente);

                Profissional profissional = new Profissional(registroAgendamento.getNomeMedico(), registroAgendamento.getEspecialidade());
                profissional = profissionalService.buscarOuCriarMedico(profissional);

                Consulta consulta = new Consulta(paciente, profissional, registroAgendamento.getDataAgendamento(), registroAgendamento.getHoraAgendamento(),
                                                        registroAgendamento.getLinkConsulta(), registroAgendamento.getCodigoConsulta(), registroAgendamento.getObsAgendamento());
                consultaService.buscarOuCriarConsulta(consulta);
        }

        } catch (Exception e) {
            logger.error("Erro ao processar a planilha: " + e.getMessage(), e);
            return null;
        }

        return listasAgendamentos;
    }

}
