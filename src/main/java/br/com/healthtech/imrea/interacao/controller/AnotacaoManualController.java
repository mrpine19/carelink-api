package br.com.healthtech.imrea.interacao.controller;

import br.com.healthtech.imrea.interacao.dto.AnotacaoInputDTO;
import br.com.healthtech.imrea.interacao.service.AnotacaoManualService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/anotacoes")
public class AnotacaoManualController {

    @Inject
    AnotacaoManualService anotacaoManualService;

    @POST
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response salvarAnotacao(AnotacaoInputDTO anotacaoInputDTO) {
        anotacaoManualService.salvarAnotacao(anotacaoInputDTO);
        return Response.ok().build();
    }
}
