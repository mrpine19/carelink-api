package br.com.healthtech.imrea.alerta.service;

import br.com.healthtech.imrea.consulta.service.ConsultaService;
import br.com.healthtech.imrea.interacao.domain.TipoInteracao;
import br.com.healthtech.imrea.interacao.service.TemplateMensagemService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@ApplicationScoped
public class AlertaService {

    @Inject
    ChatbotService chatbotService;

    @Inject
    ConsultaService consultaService;

    @Inject
    TemplateMensagemService templateService;

    private static final String PYTHON_API_URL = "https://carelink-bot-whatsapp.onrender.com/ask";

    public void processarMensagemWebhook(String payload) {
        try (JsonReader reader = Json.createReader(new StringReader(payload))) {
            JsonObject jsonPayload = reader.readObject();

            if (jsonPayload.containsKey("messages")) {
                JsonArray messages = jsonPayload.getJsonArray("messages");
                for (JsonObject message : messages.getValuesAs(JsonObject.class)) {
                    processarMensagemIndividual(message);
                }
            }
        } catch (Exception e) {
            // Em um cenário real, logar a exceção é crucial
            e.printStackTrace();
        }
    }

    private void processarMensagemIndividual(JsonObject mensagem) {
        // Garante que não é uma mensagem enviada pelo próprio bot e não é uma resposta a botões
        if (!mensagem.getBoolean("from_me", false) && !mensagem.containsKey("reply")) {
            String telefonePaciente = mensagem.getString("from");
            String textoMensagem = mensagem.getJsonObject("text").getString("body");

            String respostaIA = chamarApiRagPython(telefonePaciente, textoMensagem);

            if (respostaIA != null && !respostaIA.isEmpty()) {
                chatbotService.enviarMensagem(telefonePaciente, respostaIA);
            } else {
                String mensagemErro = "Desculpe, não consegui processar sua pergunta no momento. Tente novamente mais tarde.";
                chatbotService.enviarMensagem(telefonePaciente, mensagemErro);
            }
        } else if (mensagem.containsKey("reply")) {
            processarRespostaBotao(mensagem);
        }
    }

    private void processarRespostaBotao(JsonObject mensagem) {
        JsonObject replyObject = mensagem.getJsonObject("reply");

        if (replyObject.containsKey("buttons_reply")) {
            JsonObject buttonsReply = replyObject.getJsonObject("buttons_reply");
            String buttonId = buttonsReply.getString("id");
            String telefonePaciente = formatarTelefone(mensagem.getString("from"));
            String telefoneDestino = mensagem.getString("from");

            if (buttonId.contains("CONFIRM_PRESENCE_SIM")) {
                consultaService.confirmarPresenca(telefonePaciente);
                JsonObject payloadConfirmacao = templateService.construirMensagemConfirmarConsulta();
                payloadConfirmacao = Json.createObjectBuilder(payloadConfirmacao).add("to", telefoneDestino).build();
                chatbotService.enviarMensagem(payloadConfirmacao, TipoInteracao.CONFIRMAR_CONSULTA);

            } else if (buttonId.contains("CONFIRM_PRESENCE_NAO")) {
                consultaService.pacientePrecisaReagendar(telefonePaciente);
                JsonObject payloadReagendamento = templateService.construirMensagemReagendarConsulta();
                payloadReagendamento = Json.createObjectBuilder(payloadReagendamento).add("to", telefoneDestino).build();
                chatbotService.enviarMensagem(payloadReagendamento, TipoInteracao.REAGENDAR_CONSULTA);
            }
        }
    }

    private String chamarApiRagPython(String idUsuario, String pergunta) {
        HttpClient client = HttpClient.newHttpClient();
        String jsonPayload = String.format(
                "{\"userId\": \"%s\", \"question\": \"%s\"}",
                idUsuario,
                pergunta.replace("\"", "\\\"")
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(PYTHON_API_URL))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(30))
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return extrairRespostaDoJson(response.body());
            } else {
                System.err.println("Erro na chamada da API Python. Status: " + response.statusCode() + " | Body: " + response.body());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String extrairRespostaDoJson(String jsonResponse) {
        try (JsonReader reader = Json.createReader(new StringReader(jsonResponse))) {
            JsonObject jsonObject = reader.readObject();
            return jsonObject.getString("answer", null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String formatarTelefone(String telefone) {
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
}
