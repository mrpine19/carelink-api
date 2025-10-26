package br.com.healthtech.imrea.interacao.service;

import br.com.healthtech.imrea.agendamento.domain.Consulta;
import br.com.healthtech.imrea.agendamento.service.ConsultaService;
import br.com.healthtech.imrea.alerta.service.ChatbotService;
import br.com.healthtech.imrea.interacao.domain.TipoInteracao;
import br.com.healthtech.imrea.interacao.dto.InteracaoSistemaDTO;
import br.com.healthtech.imrea.paciente.domain.Paciente;
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

    private void enviarLembrete(List<Consulta> consultas, TipoInteracao tipo) {
        for (Consulta consulta : consultas) {
            try {
                Paciente paciente = consulta.getPaciente();
                logger.debug("Iniciando processo de lembrete para a consulta {}", consulta.getIdConsulta());

                // 1. Envia para o paciente
                enviarMensagemParaDestinatario(consulta, tipo, paciente.getNomePaciente(), paciente.getTelefonePaciente());

                // 2. Envia para os cuidadores, se existirem
                Set<Cuidador> cuidadores = paciente.getCuidadores();
                String receptorTipo = "PACIENTE";
                if (cuidadores == null || cuidadores.isEmpty() || cuidadores.stream().allMatch(c -> c == null)) {
                    logger.warn("Paciente {} não possui cuidadores para a consulta {}", paciente.getNomePaciente(), consulta.getIdConsulta());
                } else {
                    enviarMensagensParaCuidadores(consulta, tipo);
                    receptorTipo = "AMBOS";
                }

                // 3. Registra que a interação ocorreu
                registrarInteracao(consulta, tipo, receptorTipo);
            } catch (Exception e) {
                logger.error("Falha crítica no processo de envio de lembrete para a consulta {}: {}", consulta.getIdConsulta(), e.getMessage(), e);
            }
        }
    }

    private void enviarMensagensParaCuidadores(Consulta consulta, TipoInteracao tipo) {
        for (Cuidador cuidador : consulta.getPaciente().getCuidadores()) {
            try {
                logger.info("Enviando mensagem para cuidador: {}, consulta: {}", cuidador.getNomeCuidador(), consulta.getIdConsulta());
                enviarMensagemParaDestinatario(consulta, tipo, cuidador.getNomeCuidador(), cuidador.getTelefoneCuidador());
            } catch (Exception e) {
                logger.error("Erro ao enviar mensagem para cuidador {}: {}", cuidador.getNomeCuidador(), e.getMessage());
            }
        }
    }

    private void enviarMensagemParaDestinatario(Consulta consulta, TipoInteracao tipo, String nomeDestinatario, String telefone) {
        String telefoneNormalizado = normalizarTelefone(telefone);
        if (telefoneNormalizado.isEmpty()) {
            logger.warn("Telefone do destinatário '{}' está vazio ou é inválido. Mensagem não enviada.", nomeDestinatario);
            return;
        }

        JsonObject payloadBuilder = templateService.construirMensagem(consulta, nomeDestinatario, tipo);
        if (payloadBuilder == null) {
            logger.error("Não foi possível construir o template do tipo {} para {}", tipo, nomeDestinatario);
            return;
        }

        payloadBuilder = Json.createObjectBuilder(payloadBuilder)
                .add("to", telefoneNormalizado)
                .build();

        chatbotService.enviarMensagem(payloadBuilder, tipo);
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
        novaInteracao.setDetalhesInteracao(gerarDetalhesInteracao(tipo, receptorTipo));
        novaInteracao.setDataHoraInteracao(LocalDateTime.now());
        novaInteracao.persist();
        logger.info("Interação automatizada persistida para consulta {}.", consulta.getIdConsulta());
    }

    private String gerarDetalhesInteracao(TipoInteracao tipo, String receptorTipo) {
        String tipoLembrete = tipo == TipoInteracao.LEMBRETE_24H ? "24h" : "1h";
        String paraQuem;
        if ("AMBOS".equals(receptorTipo)) {
            paraQuem = "o Paciente e Cuidador(es)";
        } else {
            paraQuem = "o Paciente";
        }
        return String.format("Lembrete de consulta (%s) enviado com sucesso via Chatbot para %s.", tipoLembrete, paraQuem);
    }

    private String normalizarTelefone(String telefone) {
        if (telefone == null || telefone.isBlank()) {
            return "";
        }
        String apenasDigitos = telefone.replaceAll("\\D", "");

        if (apenasDigitos.length() >= 10 && !apenasDigitos.startsWith("55")) {
            return "55" + apenasDigitos;
        }

        return apenasDigitos;
    }
}
