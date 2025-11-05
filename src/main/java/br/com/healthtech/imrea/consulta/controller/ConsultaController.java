package br.com.healthtech.imrea.consulta.controller;

import br.com.healthtech.imrea.consulta.dto.ConsultaDTO;
import br.com.healthtech.imrea.consulta.service.ConsultaService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
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

    /*
    return List.of(
                Map.ofEntries(
                        Map.entry("Data agenda", "2025-11-04"),
                        Map.entry("Hora Agenda", "10:00"),
                        Map.entry("Nome paciente", "João Silva"),
                        Map.entry("Número celular", "(11) 99999-0000"),
                        Map.entry("Data nascimento", "1990-05-12"),
                        Map.entry("Nome acompanhante", "Maria Silva"),
                        Map.entry("Número acompanhante", "(11) 98888-0000"),
                        Map.entry("Nome medico", "Dr. Carlos Mendes"),
                        Map.entry("Especialidade", "Fisioterapia"),
                        Map.entry("Link da consulta", "https://link.teste/joao"),
                        Map.entry("Código da consulta", 101),
                        Map.entry("anotações", "Sessão de reabilitação pós-cirúrgica.")
                ),
                Map.ofEntries(
                        Map.entry("Data agenda", "2025-11-04"),
                        Map.entry("Hora Agenda", "14:00"),
                        Map.entry("Nome paciente", "Ana Souza"),
                        Map.entry("Número celular", "(11) 97777-0000"),
                        Map.entry("Data nascimento", "1985-09-30"),
                        Map.entry("Nome acompanhante", "—"),
                        Map.entry("Número acompanhante", "—"),
                        Map.entry("Nome medico", "Dra. Paula Ribeiro"),
                        Map.entry("Especialidade", "Psicologia"),
                        Map.entry("Link da consulta", "https://link.teste/ana"),
                        Map.entry("Código da consulta", 102),
                        Map.entry("anotações", "Sessão de acompanhamento emocional.")
                ),
                Map.ofEntries(
                        Map.entry("Data agenda", "2025-11-05"),
                        Map.entry("Hora Agenda", "09:00"),
                        Map.entry("Nome paciente", "Pedro Lima"),
                        Map.entry("Número celular", "(11) 96666-0000"),
                        Map.entry("Data nascimento", "1995-04-22"),
                        Map.entry("Nome acompanhante", ""),
                        Map.entry("Número acompanhante", ""),
                        Map.entry("Nome medico", "Dr. Marcelo Torres"),
                        Map.entry("Especialidade", "Fonoaudiologia"),
                        Map.entry("Link da consulta", "https://link.teste/pedro"),
                        Map.entry("Código da consulta", 103),
                        Map.entry("anotações", "Treino de dicção e fluência verbal.")
                ));
     */
}
