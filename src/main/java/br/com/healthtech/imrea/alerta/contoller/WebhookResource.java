package br.com.healthtech.imrea.alerta.contoller;

import br.com.healthtech.imrea.alerta.service.ChatbotService;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.StringReader;

@Path("/whapi-webhook")
public class WebhookResource {

    @Inject
    ChatbotService chatbotService; // Injeta o serviço de envio de mensagens

    @POST
    @Path("/send-message")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendMessageFromEndpoint(String payload) {
        try (JsonReader reader = Json.createReader(new StringReader(payload))) {
            JsonObject jsonPayload = reader.readObject();

            String to = jsonPayload.getString("to");
            String body = jsonPayload.getString("body");

            chatbotService.sendMessage(to, body);
            return Response.ok("Mensagem enviada com sucesso!").build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Erro ao processar a requisição: " + e.getMessage()).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response handleWebhook(String payload) {
        try (JsonReader reader = Json.createReader(new StringReader(payload))) {
            JsonObject jsonPayload = reader.readObject();
            JsonArray messages = jsonPayload.getJsonArray("messages");

            // Refatoração: Agora checamos diretamente as chaves de evento
            if (jsonPayload.containsKey("messages")) {
                for (JsonObject message : messages.getValuesAs(JsonObject.class)) {
                    // Adicionada uma verificação para não responder a mensagens que o próprio bot enviou
                    if (message.getBoolean("from_me", false) == false) {
                        String fromUserId = message.getString("from");
                        String messageBody = message.getJsonObject("text").getString("body");

                        System.out.println("\n--- Mensagem Recebida! ---");
                        System.out.println("De: " + fromUserId);
                        System.out.println("Mensagem: " + messageBody);
                        System.out.println("--------------------------\n");

                        String replyMessage = "Olá! Você disse: \"" + messageBody + "\".";
                        chatbotService.sendMessage(fromUserId, replyMessage);
                    }
                }
            }

            // Refatoração: Checamos diretamente a chave 'statuses'
            else if (jsonPayload.containsKey("statuses")) {
                JsonArray statuses = jsonPayload.getJsonArray("statuses");
                for (JsonObject statusObject : statuses.getValuesAs(JsonObject.class)) {
                    if ("read".equals(statusObject.getString("status", null))) {
                        String messageId = statusObject.getString("id");
                        String userId = statusObject.getString("recipient_id");

                        System.out.println("--- Mensagem Visualizada! ---");
                        System.out.println("ID da Mensagem: " + messageId);
                        System.out.println("Visualizada por: " + userId);
                        System.out.println("----------------------------\n");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.ok().build();
    }
}