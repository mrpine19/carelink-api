package br.com.healthtech.imrea.alerta.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@ApplicationScoped
public class ChatbotService {

    @ConfigProperty(name = "whapi.api.token")
    String apiToken;

    @ConfigProperty(name = "whapi.api.instance-id")
    String instanceId;

    private final HttpClient client = HttpClient.newHttpClient();

    public void sendMessage(String to, String body) {
        // Constrói o corpo da requisição JSON para enviar a mensagem
        JsonObject payload = Json.createObjectBuilder()
                .add("to", to)
                .add("body", body)
                .build();

        String url = String.format("https://gate.whapi.cloud/messages/text?instance_id=%s", instanceId);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiToken) // Seu token é adicionado aqui
                .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Mensagem enviada com sucesso! Resposta da Whapi: " + response.body());
            } else {
                System.err.println("Erro ao enviar mensagem. Status: " + response.statusCode());
                System.err.println("Resposta da Whapi: " + response.body());
            }
        } catch (Exception e) {
            System.err.println("Ocorreu um erro ao enviar a mensagem: " + e.getMessage());
            e.printStackTrace();
        }
    }
}