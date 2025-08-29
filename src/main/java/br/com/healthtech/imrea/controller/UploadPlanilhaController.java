package br.com.healthtech.imrea.controller;

import br.com.healthtech.imrea.service.UploadPlanilhaService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/api/upload")
public class UploadPlanilhaController {

    private final UploadPlanilhaService uploadPlanilhaService;

    public UploadPlanilhaController(UploadPlanilhaService uploadPlanilhaService) {
        this.uploadPlanilhaService = uploadPlanilhaService;
    }

    @GET
    public Response getUploadPlanilha() {
        uploadPlanilhaService.processarPlanilha();
        return Response.ok().build();
    }
}
