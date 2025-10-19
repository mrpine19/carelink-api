package br.com.healthtech.imrea.agendamento.service;

import br.com.healthtech.imrea.agendamento.domain.Consulta;
import br.com.healthtech.imrea.agendamento.domain.Profissional;
import br.com.healthtech.imrea.agendamento.domain.RegistroAgendamento;
import br.com.healthtech.imrea.agendamento.domain.UploadLog;
import br.com.healthtech.imrea.interacao.domain.TipoInteracao;
import br.com.healthtech.imrea.interacao.service.InteracaoAutomatizadaService;
import br.com.healthtech.imrea.paciente.domain.Paciente;
import br.com.healthtech.imrea.usuario.service.UsuarioService;
import com.alibaba.excel.EasyExcel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class UploadPlanilhaService {

    private static final Logger logger = LoggerFactory.getLogger(UploadPlanilhaService.class);

    @Inject
    AgendamentoMapper agendamentoMapper;

    @Inject
    UsuarioService usuarioService;

    List<Consulta> listaConsultas = new ArrayList<>();

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
                        .peek(RegistroAgendamento::normalizarCamposNulos)
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

    @Transactional
    public void salvarDadosAgendamento(List<RegistroAgendamento> listasAgendamentos) {

        UploadLog uploadLog = new UploadLog();
        uploadLog.dataHoraUpload = LocalDateTime.now();
        uploadLog.statusUpload = "EM_PROCESSAMENTO";
        uploadLog.usuario = usuarioService.buscarUsuarioTeste();
        uploadLog.persist();

        StringBuilder detalhesErrosBuilder = new StringBuilder();

        /* REMOVER ESSA LINHA AQUI DEPOIS */
        listaConsultas = new ArrayList<>();

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
        Paciente paciente = agendamentoMapper.salvarInformacoesPaciente(registro);
        Profissional profissional = agendamentoMapper.salvarInformacoesProfissional(registro);
        Consulta consulta = agendamentoMapper.salvarInformacoesConsulta(registro, paciente, profissional, uploadLog);

        /* REMOVER ESSA LINHA AQUI DEPOIS */
        listaConsultas.add(consulta);
    }
}