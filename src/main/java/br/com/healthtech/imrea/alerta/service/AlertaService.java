package br.com.healthtech.imrea.alerta.service;

import br.com.healthtech.imrea.consulta.domain.Consulta;
import br.com.healthtech.imrea.consulta.service.ConsultaService;
import br.com.healthtech.imrea.interacao.domain.TipoInteracao;
import br.com.healthtech.imrea.interacao.service.TemplateMensagemService;
import br.com.healthtech.imrea.paciente.domain.Paciente;
import br.com.healthtech.imrea.paciente.service.PacienteService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import org.eclipse.microprofile.config.inject.ConfigProperty; // Importar ConfigProperty

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

    @Inject
    PacienteService pacienteService;

    @Inject
    @ConfigProperty(name = "python.rag.api.url", defaultValue = "http://127.0.0.1:5000/ask")
    String urlApiPython;

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
                .uri(URI.create(urlApiPython)) // Usando a variável injetada
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

    public void enviarLembretePaciente(String idPaciente) {
        Paciente paciente = pacienteService.buscarPacientePorId(Integer.parseInt(idPaciente));
        if (paciente == null) {
            System.err.println("Erro: Paciente com ID " + idPaciente + " não encontrado.");
            // Opcional: lançar uma exceção ou retornar um erro mais específico
            return;
        }

        Consulta consulta = consultaService.buscarConsultaMaisRecentePorPaciente(paciente);
        if (consulta == null) {
            System.err.println("Erro: Nenhuma consulta recente encontrada para o paciente com ID " + idPaciente + ".");
            // Opcional: lançar uma exceção ou retornar um erro mais específico
            return;
        }

        String telefonePaciente = normalizarTelefone(paciente.getTelefonePaciente()); // Assumindo que Paciente tem um método getTelefone()
        if (telefonePaciente.isEmpty()) {
            System.err.println("Erro: Telefone do paciente com ID " + idPaciente + " não encontrado.");
            return;
        }

        // 1. Construir o conteúdo da mensagem usando o templateService
        JsonObject mensagemConteudo = templateService.construirMensagem(consulta, paciente.getNomePaciente(), TipoInteracao.LEMBRETE_1H);

        // 2. Criar o payload final adicionando a propriedade "to"
        JsonObject payloadFinal = Json.createObjectBuilder(mensagemConteudo)
                .add("to", telefonePaciente) // Adiciona o telefone do paciente como destinatário
                .build();

        // 3. Enviar a mensagem
        chatbotService.enviarMensagem(payloadFinal, TipoInteracao.LEMBRETE_1H);
        System.out.println("Lembrete enviado com sucesso para o paciente: " + paciente.getNomePaciente() + " (" + telefonePaciente + ")");
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
