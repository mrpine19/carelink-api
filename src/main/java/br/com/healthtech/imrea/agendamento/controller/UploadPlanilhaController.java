package br.com.healthtech.imrea.agendamento.controller;

import br.com.healthtech.imrea.agendamento.domain.FormData;
import br.com.healthtech.imrea.agendamento.domain.RegistroAgendamento;
import br.com.healthtech.imrea.agendamento.service.UploadPlanilhaService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/upload")
public class UploadPlanilhaController {

    @Inject
    UploadPlanilhaService uploadPlanilhaService;

    @POST
    @Path("/receber")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response receberArquivo(FormData arquivoRecebido) {
        List<RegistroAgendamento> listasAgendamentos = uploadPlanilhaService.processarPlanilha(arquivoRecebido.fileUpload);
        return Response.ok(listasAgendamentos).build();
    }

    @POST
    @Path("/salvar")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response salvarDadosAgendamento(List<RegistroAgendamento> agendamentos)  {
        try{
            uploadPlanilhaService.salvarDadosAgendamento(agendamentos);
            return Response.ok().build();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao salvar os dados: " + e.getMessage())
                    .build();
        }
    }
}
