package br.com.healthtech.imrea.alerta.controller;

import br.com.healthtech.imrea.alerta.service.ChatbotService;
import br.com.healthtech.imrea.consulta.service.ConsultaService;
import br.com.healthtech.imrea.interacao.domain.TipoInteracao;
import br.com.healthtech.imrea.interacao.service.TemplateMensagemService;
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
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Path("/whapi-webhook")
public class WebhookResource {

    @Inject
    ChatbotService chatbotService; // Injeta o serviço de envio de mensagens

    @Inject
    ConsultaService consultaService;

    @Inject
    TemplateMensagemService templateService;

    private static final String PYTHON_API_URL = "http://127.0.0.1:5000/ask";

    @POST
    @Path("/send-message")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendMessageFromEndpoint(String payload) {
        try (JsonReader reader = Json.createReader(new StringReader(payload))) {
            JsonObject jsonPayload = reader.readObject();

            String to = jsonPayload.getString("to");
            String body = jsonPayload.getString("body");

            chatbotService.enviarMensagem(to, body);
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

            if (jsonPayload.containsKey("messages")) {
                JsonArray messages = jsonPayload.getJsonArray("messages");
                for (JsonObject message : messages.getValuesAs(JsonObject.class)) {
                    // Garante que não é uma mensagem enviada pelo próprio bot
                    if (!message.getBoolean("from_me", false)) {
                        String telefonePaciente = message.getString("from");
                        String mensagemPaciente = message.getJsonObject("text").getString("body");

                        // 1. CHAMA A API DO PYTHON COM A MENSAGEM RECEBIDA
                        String answerFromAI = callPythonRagAPI(telefonePaciente, mensagemPaciente);

                        // 2. ENVIA A RESPOSTA DA IA DE VOLTA PARA O USUÁRIO
                        if (answerFromAI != null && !answerFromAI.isEmpty()) {
                            chatbotService.enviarMensagem(telefonePaciente, answerFromAI);
                        } else {
                            // Envia uma mensagem de erro padrão caso a API falhe
                            String errorMessage = "Desculpe, não consegui processar sua pergunta no momento. Tente novamente mais tarde.";
                            chatbotService.enviarMensagem(telefonePaciente, errorMessage);
                        }
                    }
                    if (message.containsKey("reply")) {

                        JsonObject replyObject = message.getJsonObject("reply");

                        if (replyObject.containsKey("buttons_reply")) {
                            JsonObject buttonsReply = replyObject.getJsonObject("buttons_reply");

                            String buttonId = buttonsReply.getString("id");
                            String telefonePaciente = formatarTelefone(message.getString("from"));

                            if (buttonId.contains("CONFIRM_PRESENCE_SIM")) {
                                consultaService.confirmarPresenca(telefonePaciente);
                                JsonObject payloadBuilder = templateService.construirMensagemConfirmarConsulta();
                                payloadBuilder = Json.createObjectBuilder(payloadBuilder)
                                        .add("to", message.getString("from"))
                                        .build();

                                chatbotService.enviarMensagem(payloadBuilder, TipoInteracao.CONFIRMAR_CONSULTA);


                            } else if (buttonId.contains("CONFIRM_PRESENCE_NAO")) {
                                consultaService.pacientePrecisaReagendar(telefonePaciente);
                                JsonObject payloadBuilder = templateService.construirMensagemReagendarConsulta();
                                payloadBuilder = Json.createObjectBuilder(payloadBuilder)
                                        .add("to", message.getString("from"))
                                        .build();

                                chatbotService.enviarMensagem(payloadBuilder, TipoInteracao.REAGENDAR_CONSULTA);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.ok().build();
    }

    public static String formatarTelefone(String telefone) {
        if (telefone == null || telefone.length() < 10) {
            throw new IllegalArgumentException("Telefone deve ter pelo menos 10 dígitos");
        }

        String numero = telefone;
        if (telefone.length() == 13 && telefone.startsWith("55")) {
            numero = telefone.substring(2);
        }

        if (numero.length() == 11) {
            return String.format("(%s) %s-%s",
                    numero.substring(0, 2),
                    numero.substring(2, 7),
                    numero.substring(7));
        } else if (numero.length() == 10) {
            return String.format("(%s) %s-%s",
                    numero.substring(0, 2),
                    numero.substring(2, 6),
                    numero.substring(6));
        } else {
            throw new IllegalArgumentException("Formato de telefone não suportado: " + telefone);
        }
    }

    /**
     * Método responsável por fazer a chamada HTTP para a API RAG em Python.
     *
     * @param userId O ID do usuário (para rastreamento).
     * @param question A pergunta feita pelo usuário.
     * @return A resposta gerada pela IA, ou null em caso de erro.
     */
    private String callPythonRagAPI(String userId, String question) {
        // Usando o HttpClient nativo do Java 11+
        HttpClient client = HttpClient.newHttpClient();

        // Monta o corpo da requisição (payload) no formato JSON esperado pelo Python
        String jsonPayload = String.format(
                "{\"userId\": \"%s\", \"question\": \"%s\"}",
                userId,
                // Escapa aspas duplas na pergunta para evitar um JSON inválido
                question.replace("\"", "\\\"")
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(PYTHON_API_URL))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(30)) // Timeout de 30 segundos
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        try {
            System.out.println("Enviando para a API Python: " + jsonPayload);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Resposta da API Python: " + response.body());
                // Extrai apenas o valor da chave "answer" do JSON de resposta
                return parseAnswerFromJson(response.body());
            } else {
                System.err.println("Erro na chamada da API Python. Status: " + response.statusCode());
                System.err.println("Resposta: " + response.body());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Extrai o valor da chave "answer" de um payload JSON.
     * Em um projeto real, é altamente recomendado usar uma biblioteca como
     * Jakarta JSON Binding, Gson ou Jackson para fazer isso de forma segura.
     */
    private String parseAnswerFromJson(String jsonResponse) {
        try (JsonReader reader = Json.createReader(new StringReader(jsonResponse))) {
            JsonObject jsonObject = reader.readObject();
            return jsonObject.getString("answer", null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}