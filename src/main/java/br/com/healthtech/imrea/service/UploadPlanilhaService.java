package br.com.healthtech.imrea.service;

import br.com.healthtech.imrea.domain.RegistroAgendamento;
import br.com.healthtech.imrea.domain.UploadLog;
import com.alibaba.excel.EasyExcel;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

@ApplicationScoped
public class UploadPlanilhaService {

    private static final Logger logger = LoggerFactory.getLogger(UploadPlanilhaService.class);

    public void processarPlanilha() {

        String nomeArquivo = "PlanilhaFicticia.xlsx";
        //UploadLog uploadLog = new UploadLog(1L, "Planilha", Date.from(Instant.now()));
        logger.info("Processando planilha do arquivo {}", nomeArquivo);
        try {
            if (nomeArquivo.toLowerCase().endsWith(".xlsx")) {
                processarXlsx();
            } else {
            }

        } catch (Exception e) {
            logger.info("Erro ao processar planilha");
        }
    }

    public void processarXlsx()  {
        String arquivoXLSX = "C:\\Dev\\Planilha fictícia de acompanhamento.xlsx";
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


}
