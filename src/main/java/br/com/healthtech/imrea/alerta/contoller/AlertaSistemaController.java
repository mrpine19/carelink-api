package br.com.healthtech.imrea.alerta.contoller;

import br.com.healthtech.imrea.alerta.dto.AlertaDTO;
import br.com.healthtech.imrea.alerta.service.AlertaSistemaService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/alertas/hoje")
public class AlertaSistemaController {

    @Inject
    AlertaSistemaService alertaSistemaService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response obterAlertasHoje(){
        List<AlertaDTO> alertaDTO = alertaSistemaService.obterAlertasHoje();
        return Response.ok(alertaDTO).build();
    }
}
