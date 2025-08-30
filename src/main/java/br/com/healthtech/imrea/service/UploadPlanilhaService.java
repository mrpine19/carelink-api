package br.com.healthtech.imrea.service;

import br.com.healthtech.imrea.domain.RegistroAgendamento;
import br.com.healthtech.imrea.domain.UploadLog;
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

    public void processarPlanilha(FileUpload fileUpload) {

        String nomeArquivo = "PlanilhaFicticia.xlsx";
        //UploadLog uploadLog = new UploadLog(1L, "Planilha", Date.from(Instant.now()));
        logger.info("Processando planilha do arquivo {}", nomeArquivo);
        try {
            if (nomeArquivo.toLowerCase().endsWith(".xlsx")) {
                processarXlsx(fileUpload.uploadedFile().toFile());
            } else {
            }

        } catch (Exception e) {
            logger.error("Erro ao processar planilha");
            logger.error(e.getMessage());
        }
    }
    /* COMENTANDO UPLOAD LOCAL
    public void processarXlsx()  {
        String arquivoXLSX = "C:\\Devv\\Planilha fictícia de acompanhamento.xlsx";
        List<RegistroAgendamento> listasAgendamentos = EasyExcel.read(new File(arquivoXLSX))
                .head(RegistroAgendamento.class)
                .sheet()
                .doReadSync();

        logger.info("Total de produtos lidos: " + listasAgendamentos.size());
        for (RegistroAgendamento registroAgendamento : listasAgendamentos) {
            logger.info("Nome do paciente: "+registroAgendamento.getNomePaciente()+" - Nome do médico: "+registroAgendamento.getNomeMedico()
                    + " - Data agendada: "+registroAgendamento.getDataAgendamento()+" às " +registroAgendamento.getHoraAgendamento());
        }
    }
    COMENTANDO UPLOAD LOCAL */

    public void processarXlsx(File planilha)  {
        List<RegistroAgendamento> listasAgendamentos;
        try {
            listasAgendamentos = EasyExcel.read(planilha)
                    .head(RegistroAgendamento.class)
                    .sheet()
                    .doReadSync();

            logger.info("Total de registros lidos: " + listasAgendamentos.size());
            for (RegistroAgendamento registroAgendamento : listasAgendamentos) {
                logger.info("Nome do paciente: "+registroAgendamento.getNomePaciente()+" - Nome do médico: "+registroAgendamento.getNomeMedico()
                        + " - Data agendada: "+registroAgendamento.getDataAgendamento()+" às " +registroAgendamento.getHoraAgendamento());
            }

        } catch (Exception e) {
            logger.error("Erro ao processar a planilha: " + e.getMessage(), e);
        }
    }

}
