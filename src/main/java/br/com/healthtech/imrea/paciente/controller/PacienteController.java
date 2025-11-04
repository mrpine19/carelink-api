package br.com.healthtech.imrea.paciente.controller;

import br.com.healthtech.imrea.paciente.domain.Paciente;
import br.com.healthtech.imrea.paciente.dto.PacienteDTO;
import br.com.healthtech.imrea.paciente.service.PacienteService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/paciente")
public class PacienteController {

    @Inject
    PacienteService pacienteService;

    @GET
    @Path("/{idPaciente}/historico")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obterHistoricoPaciente(@PathParam("idPaciente") Long idPaciente) {
        PacienteDTO pacienteDTO = pacienteService.buscarHistoricoPacientePorId(idPaciente);
        return Response.ok(pacienteDTO).build();
    }
}
