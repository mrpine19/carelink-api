package br.com.healthtech.imrea.consulta.controller;

import br.com.healthtech.imrea.consulta.dto.ConsultaDTO;
import br.com.healthtech.imrea.consulta.dto.ConsultaUpdateDTO;
import br.com.healthtech.imrea.consulta.service.ConsultaService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/agendamentos")
public class ConsultaController {

    @Inject
    ConsultaService consultaService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarConsultasPorPeriodo(@QueryParam("dataInicio") String dataInicio, @QueryParam("dataFim") String dataFim){
        List<ConsultaDTO> consultaDTO = consultaService.buscarConsultasPorPeriodo(dataInicio, dataFim);
        return Response.ok(consultaDTO).build();
    }

    @PUT
    @Path("/{idConsulta}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response atualizarConsulta(@PathParam("idConsulta") Long idConsulta, ConsultaUpdateDTO consultaUpdateDTO){
        consultaService.atualizarConsulta(idConsulta, consultaUpdateDTO);
        return Response.ok(consultaUpdateDTO).build();
    }
}
