package br.com.healthtech.imrea.interacao.service;

import br.com.healthtech.imrea.agendamento.domain.Consulta;
import br.com.healthtech.imrea.agendamento.service.ConsultaService;
import br.com.healthtech.imrea.alerta.service.ChatbotService;
import br.com.healthtech.imrea.interacao.domain.TipoInteracao;
import br.com.healthtech.imrea.interacao.dto.InteracaoSistemaDTO;
import br.com.healthtech.imrea.interacao.dto.LembreteConsultaDTO;
import br.com.healthtech.imrea.paciente.domain.Cuidador;
import br.com.healthtech.imrea.interacao.domain.InteracaoAutomatizada;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
        List<Consulta> consultasDeAmanha = consultaService.buscarConsultasMarcadasDiaSeguinte();
        if (!consultasDeAmanha.isEmpty()){
            logger.info("Iniciando envio de lembrete 24h para consultas de amanhã.");
            enviarLembrete(consultasDeAmanha, TipoInteracao.LEMBRETE_24H);
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 6-18 * * ?") // Executa a cada hora entre 6h e 18h
    public void enviarLembrete1HoraConsulta() {
        List<Consulta> consultasDeAgora = consultaService.buscarConsultasMarcadasProximaHora();
        if (!consultasDeAgora.isEmpty()){
            logger.info("Iniciando envio de lembrete 1h para consultas da próxima hora.");
            enviarLembrete(consultasDeAgora, TipoInteracao.LEMBRETE_1H);
        }
    }

    /* private */ public void enviarLembrete(List<Consulta> consultas, TipoInteracao tipo) {
        for (Consulta consulta : consultas) {
            try {
                logger.info("Enviando mensagem para paciente: {}, consulta: {}", consulta.getPaciente().getNomePaciente(), consulta.getIdConsulta());
                JsonObject payload = templateService.construirMensagem(consulta, consulta.getPaciente().getNomePaciente(), tipo);
                payload = Json.createObjectBuilder(payload)
                        .add("to", normalizarTelefone(consulta.getPaciente().getTelefonePaciente())).build();
                chatbotService.enviarMensagem(payload, tipo);

                Set<Cuidador> cuidadores = consulta.getPaciente().getCuidadores();
                String receptorTipo = "PACIENTE";
                if (cuidadores == null || cuidadores.isEmpty() || cuidadores.stream().allMatch(c -> c == null)) {
                    logger.warn("Paciente {} não possui cuidadores.", consulta.getPaciente().getNomePaciente());
                } else {
                    enviaMensagemCuidador(consulta, tipo);
                    receptorTipo = "AMBOS";
                }

                registrarInteracao(consulta, tipo, receptorTipo);
            } catch (Exception e) {
                logger.error("Erro ao enviar mensagem para paciente {}: {}", consulta.getPaciente().getNomePaciente(), e.getMessage());
            }
        }
    }

    public void enviaMensagemCuidador(Consulta consulta, TipoInteracao tipo) {
        for (Cuidador cuidador : consulta.getPaciente().getCuidadores()) {
            try {
                logger.info("Enviando mensagem para cuidador: {}, consulta: {}", cuidador.getNomeCuidador(), consulta.getIdConsulta());
                JsonObject payload = templateService.construirMensagem(consulta, cuidador.getNomeCuidador(), tipo);
                payload = Json.createObjectBuilder(payload)
                        .add("to", normalizarTelefone(cuidador.getTelefoneCuidador())).build();
                chatbotService.enviarMensagem(payload, tipo);
            } catch (Exception e) {
                logger.error("Erro ao enviar mensagem para cuidador {}: {}", cuidador.getNomeCuidador(), e.getMessage());
            }
        }
    }

    public List<InteracaoSistemaDTO> buscarHistoricoSistemaPorPaciente(Long idPaciente) {
        List<InteracaoAutomatizada> interacoes = InteracaoAutomatizada.find("paciente.idPaciente = ?1 order by dataHoraInteracao desc", idPaciente).list();
        List<InteracaoSistemaDTO> interacoesDTO = new ArrayList<>();

        for (InteracaoAutomatizada interacao : interacoes) {
            InteracaoSistemaDTO dto = new InteracaoSistemaDTO();
            dto.setTipo("INTERACAO_SISTEMA");
            dto.setData(interacao.getDataHoraInteracao().toLocalDate().toString());
            dto.setHora(String.format("%02d:%02d", interacao.getDataHoraInteracao().getHour(), interacao.getDataHoraInteracao().getMinute()));
            dto.setLog(interacao.getDetalhesInteracao());
            interacoesDTO.add(dto);
        }
        return interacoesDTO;
    }

    public void registrarInteracao(Consulta consulta, TipoInteracao tipo, String receptorTipo){
        InteracaoAutomatizada novaInteracao = new InteracaoAutomatizada(consulta, consulta.getPaciente());
        novaInteracao.setTipoInteracao(tipo.getTipo());
        novaInteracao.setReceptorTipo(receptorTipo);
        novaInteracao.setStatusInteracao("Lembrete enviado");
        novaInteracao.setDetalhesInteracao("Lembrete de consulta (24h) enviado com sucesso via Chatbot para o Paciente.");
        novaInteracao.setDataHoraInteracao(LocalDateTime.now());
        novaInteracao.persist();
        logger.info("Interação automatizada persistida para consulta {}.", consulta.getIdConsulta());
    }

    private String normalizarTelefone(String numero) {
        return "55" + numero.replaceAll("[^0-9]", "");
    }
}
