package br.com.healthtech.imrea.controller;

import br.com.healthtech.imrea.domain.FormData;
import br.com.healthtech.imrea.service.UploadPlanilhaService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/upload")
public class UploadPlanilhaController {

    private final UploadPlanilhaService uploadPlanilhaService;

    public UploadPlanilhaController(UploadPlanilhaService uploadPlanilhaService) {
        this.uploadPlanilhaService = uploadPlanilhaService;
    }

    /* COMENTANDO UPLOAD LOCAL
    @GET
    public Response getUploadPlanilha() {
        uploadPlanilhaService.processarPlanilha();
        return Response.ok().build();
    }
    COMENTANDO UPLOAD LOCAL */

    @POST
    @Path("/receber")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response receberArquivo(FormData arquivoRecebido) {

        uploadPlanilhaService.processarPlanilha(arquivoRecebido.fileUpload);
        return Response.ok().build();
    }
}
