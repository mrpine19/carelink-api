package br.com.healthtech.imrea.interacao.service;

import br.com.healthtech.imrea.agendamento.domain.Consulta;
import br.com.healthtech.imrea.agendamento.service.ConsultaService;
import br.com.healthtech.imrea.alerta.service.ChatbotService;
import br.com.healthtech.imrea.interacao.domain.TipoInteracao;
import br.com.healthtech.imrea.paciente.domain.Cuidador;
import br.com.healthtech.imrea.interacao.domain.InteracaoAutomatizada;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class InteracaoAutomatizadaService {

    private static final Logger logger = LoggerFactory.getLogger(InteracaoAutomatizadaService.class);

    @Inject
    ConsultaService consultaService;

    @Inject
    TemplateMensagemService templateService;

    @Inject
    ChatbotService chatbotService;

    @Transactional
    @Scheduled(cron = "0 0 9 * * ?") // Executa todo dia às 9h
    public void enviarLembrete24HorasConsulta() {
        logger.info("Iniciando envio de lembrete 24h para consultas de amanhã.");
        List<Consulta> consultasDeAmanha = consultaService.buscarConsultasMarcadasDiaSeguinte();
        enviarLembrete(consultasDeAmanha, TipoInteracao.LEMBRETE_24H);
    }

    @Transactional
    @Scheduled(cron = "0 0 6-18 * * ?") // Executa a cada hora entre 6h e 18h
    public void enviarLembrete1HoraConsulta() {
        logger.info("Iniciando envio de lembrete 1h para consultas da próxima hora.");
        List<Consulta> consultasDeAgora = consultaService.buscarConsultasMarcadasProximaHora();
        enviarLembrete(consultasDeAgora, TipoInteracao.LEMBRETE_1H);
    }

    /* private */ public void enviarLembrete(List<Consulta> consultas, TipoInteracao tipo) {
        for (Consulta consulta : consultas) {
            try {
                logger.debug("Enviando mensagem para paciente: {}, consulta: {}", consulta.paciente.nomePaciente, consulta.idConsulta);
                String toPaciente = normalizarTelefone(consulta.paciente.telefonePaciente);
                String bodyPaciente = templateService.construirMensagem(consulta, consulta.paciente.nomePaciente, tipo);
                //chatbotService.sendMessage(toPaciente, bodyPaciente);

                Set<Cuidador> cuidadores = consulta.paciente.cuidadores;
                String receptorTipo = "PACIENTE";
                if (cuidadores == null || cuidadores.isEmpty() || cuidadores.stream().allMatch(c -> c == null)) {
                    logger.warn("Paciente {} não possui cuidadores.", consulta.paciente.nomePaciente);
                } else {
                    enviaMensagemCuidador(consulta, tipo);
                    receptorTipo = "AMBOS";
                }

                InteracaoAutomatizada novaInteracao = new InteracaoAutomatizada(consulta, consulta.paciente);
                novaInteracao.tipoInteracao = tipo.tipo;
                novaInteracao.receptorTipo = receptorTipo;
                novaInteracao.statusInteracao = "Lembrete enviado";
                novaInteracao.detalhesInteracao = "Enviado lembrete via chatbot";
                novaInteracao.dataHoraInteracao = LocalDateTime.now();
                novaInteracao.persist();
                logger.info("Interação automatizada persistida para consulta {}.", consulta.idConsulta);
            } catch (Exception e) {
                logger.error("Erro ao enviar mensagem para paciente {}: {}", consulta.paciente.nomePaciente, e.getMessage());
            }
        }
    }

    public void enviaMensagemCuidador(Consulta consulta, TipoInteracao tipo) {
        for (Cuidador cuidador : consulta.paciente.cuidadores) {
            try {
                logger.debug("Enviando mensagem para cuidador: {}, consulta: {}", cuidador.nomeCuidador, consulta.idConsulta);
                String to = normalizarTelefone(cuidador.telefoneCuidador);
                String body = templateService.construirMensagem(consulta, cuidador.nomeCuidador, tipo);
                //chatbotService.sendMessage(to, body);
            } catch (Exception e) {
                logger.error("Erro ao enviar mensagem para cuidador {}: {}", cuidador.nomeCuidador, e.getMessage());
            }
        }
    }

    private String normalizarTelefone(String numero) {
        return "55" + numero.replaceAll("[^0-9]", "");
    }

}
