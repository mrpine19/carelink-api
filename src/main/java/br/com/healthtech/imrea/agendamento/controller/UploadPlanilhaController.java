package br.com.healthtech.imrea.agendamento.controller;

import br.com.healthtech.imrea.agendamento.domain.FormData;
import br.com.healthtech.imrea.agendamento.domain.RegistroAgendamento;
import br.com.healthtech.imrea.agendamento.dto.UploadDTO;
import br.com.healthtech.imrea.agendamento.service.UploadPlanilhaService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/upload")
public class UploadPlanilhaController {

    private final UploadPlanilhaService uploadPlanilhaService;

    public UploadPlanilhaController(UploadPlanilhaService uploadPlanilhaService) {
        this.uploadPlanilhaService = uploadPlanilhaService;
    }

    @POST
    @Path("/receber")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response receberArquivo(FormData arquivoRecebido) {
        UploadDTO uploadDTO = uploadPlanilhaService.processarPlanilha(arquivoRecebido.fileUpload);
        return Response.ok(uploadDTO).build();
    }
}
