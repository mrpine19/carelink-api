package br.com.healthtech.imrea.alerta.controller;

import br.com.healthtech.imrea.alerta.service.AlertaService;
import br.com.healthtech.imrea.alerta.service.ChatbotService;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam; // Importar PathParam
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.StringReader;

@Path("/alerta-webhook") // Path renomeado para refletir a nova estrutura
public class AlertaController {

    @Inject
    AlertaService alertaService; // Injeta o novo serviço de negócio

    @Inject
    ChatbotService chatbotService; // Mantido para o endpoint de teste

    /**
     * Endpoint para receber e processar as mensagens do webhook.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response receberMensagem(String payload) {
        try {
            alertaService.processarMensagemWebhook(payload);
            return Response.ok().build();
        } catch (Exception e) {
            // Em um cenário real, é fundamental logar a exceção em um sistema de logs
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao processar webhook: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Endpoint de teste para enviar uma mensagem diretamente.
     * Mantido para fins de depuração e testes.
     */
    @POST
    @Path("/enviar-mensagem-teste")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response enviarMensagemTeste(String payload) {
        try (JsonReader reader = Json.createReader(new StringReader(payload))) {
            JsonObject jsonPayload = reader.readObject();
            String destinatario = jsonPayload.getString("para");
            String corpo = jsonPayload.getString("corpo");

            chatbotService.enviarMensagem(destinatario, corpo);
            return Response.ok("Mensagem de teste enviada com sucesso!").build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erro ao processar a requisição: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Endpoint para reenviar um lembrete para um paciente específico.
     * Recebe o ID do paciente como um parâmetro de caminho (path parameter).
     */
    @POST
    @Path("/enviar-lembrete/{idPaciente}") // idPaciente como path parameter
    // Não precisa de @Consumes(MediaType.APPLICATION_JSON) se não há corpo JSON
    public Response enviarLembrete(@PathParam("idPaciente") String idPaciente) { // Recebe como @PathParam
        try {
            // Delega a lógica de negócio para o AlertaService
            alertaService.enviarLembretePaciente(idPaciente);

            return Response.ok("Lembrete enviado com sucesso para o paciente: " + idPaciente).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erro ao reenviar lembrete: " + e.getMessage())
                    .build();
        }
    }
}