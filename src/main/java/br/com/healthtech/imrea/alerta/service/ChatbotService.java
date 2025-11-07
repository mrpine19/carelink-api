package br.com.healthtech.imrea.alerta.service;

import br.com.healthtech.imrea.interacao.domain.TipoInteracao;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@ApplicationScoped
public class ChatbotService {

    private static final Logger logger = LoggerFactory.getLogger(ChatbotService.class);

    @ConfigProperty(name = "whapi.api.token")
    String apiToken;

    @ConfigProperty(name = "whapi.api.instance-id")
    String instanceId;

    private final HttpClient client = HttpClient.newHttpClient();

    public void enviarMensagem(String to, String body) {
        JsonObject payload = Json.createObjectBuilder()
                .add("to", to)
                .add("body", body)
                .build();

        String url = String.format("https://gate.whapi.cloud/messages/text?instance_id=%s", instanceId);
        fazerRequisicaoHTTP(url, payload);
    }

    public void enviarMensagem(JsonObject payload, TipoInteracao tipo) {
        if (tipo == TipoInteracao.LEMBRETE_24H)
            enviarMensagemInterativa(payload);
        else if (tipo == TipoInteracao.LEMBRETE_1H || tipo == TipoInteracao.CONFIRMAR_CONSULTA || tipo == TipoInteracao.REAGENDAR_CONSULTA)
            enviarMensagemSimples(payload);
    }

    private void enviarMensagemInterativa(JsonObject payload){
        String url = String.format("https://gate.whapi.cloud/messages/interactive?instance_id=%s", instanceId);
        fazerRequisicaoHTTP(url, payload);
    }

    private void enviarMensagemSimples(JsonObject payload) {
        String url = String.format("https://gate.whapi.cloud/messages/text?instance_id=%s", instanceId);
        fazerRequisicaoHTTP(url, payload);
    }

    private void fazerRequisicaoHTTP(String url, JsonObject payload){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiToken) // Seu token é adicionado aqui
                .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                logger.info("Mensagem enviada com sucesso! Destinatário: {}, Status: {}, Resposta: {}", payload.getString("to", "N/A"), response.statusCode(), response.body());
            } else {
                logger.error("Erro ao enviar mensagem. Status: {}, Resposta: {}", response.statusCode(), response.body());
            }
        } catch (Exception e) {
            logger.error("Falha na requisição HTTP para o chatbot: {}", e.getMessage(), e);
        }
    }
}