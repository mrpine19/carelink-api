package br.com.healthtech.imrea.paciente.service;

import br.com.healthtech.imrea.agendamento.service.ConsultaService;
import br.com.healthtech.imrea.interacao.dto.InteracaoConsultaDTO;
import br.com.healthtech.imrea.interacao.dto.InteracaoEquipeDTO;
import br.com.healthtech.imrea.interacao.dto.InteracaoSistemaDTO;
import br.com.healthtech.imrea.interacao.dto.LinhaDoTempoDTO;
import br.com.healthtech.imrea.interacao.service.AnotacaoManualService;
import br.com.healthtech.imrea.interacao.service.InteracaoAutomatizadaService;
import br.com.healthtech.imrea.paciente.domain.Cuidador;
import br.com.healthtech.imrea.paciente.domain.Paciente;
import br.com.healthtech.imrea.paciente.dto.ConsultaDTO;
import br.com.healthtech.imrea.paciente.dto.CuidadorDTO;
import br.com.healthtech.imrea.paciente.dto.PacienteDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PacienteService {

    private static final Logger logger = LoggerFactory.getLogger(PacienteService.class);

    @Inject
    ConsultaService consultaService;

    @Inject
    AnotacaoManualService anotacaoManualService;

    @Inject
    InteracaoAutomatizadaService interacaoAutomatizadaService;

    @Transactional
    public Paciente buscarOuCriarPaciente(Paciente paciente){
        if (paciente.nomePaciente == null || paciente.nomePaciente.isEmpty()){
            throw new IllegalArgumentException("Nome do paciente inválido");
        }
        Paciente pacienteExistente = Paciente.find("nomePaciente = ?1 and telefonePaciente = ?2", paciente.nomePaciente, paciente.telefonePaciente).firstResult();
        if (pacienteExistente == null){
            paciente.dtCriacaoPaciente = LocalDateTime.now();
            paciente.persist();
            logger.info("Paciente {} salvo com sucesso!", paciente.nomePaciente);
            return paciente;
        }else {
            logger.info("Paciente {} já existe!", paciente.nomePaciente);
            return pacienteExistente;
        }
    }

    @Transactional
    public PacienteDTO buscarHistoricoPacientePorId(Long idPaciente) {
        if (idPaciente == null || idPaciente <= 0) {
            throw new IllegalArgumentException("ID do paciente inválido");
        }
        Paciente paciente = Paciente.findById(idPaciente);
        if (paciente == null)
            throw new IllegalArgumentException("Paciente não encontrado");

        PacienteDTO pacienteDTO = new PacienteDTO();
        pacienteDTO.setIdPaciente(paciente.idPaciente);
        pacienteDTO.setNomePaciente(paciente.nomePaciente);
        pacienteDTO.setTelefonePaciente(paciente.telefonePaciente);
        pacienteDTO.setScoreDeRisco(paciente.scoreDeRisco);

        if (pacienteDTO.getScoreDeRisco() < 40)
            pacienteDTO.setNivelDeRisco("BAIXO");
        if (pacienteDTO.getScoreDeRisco() >= 40 && pacienteDTO.getScoreDeRisco() < 75)
            pacienteDTO.setNivelDeRisco("MEDIO");
        if (pacienteDTO.getScoreDeRisco() >= 75)
            pacienteDTO.setNivelDeRisco("ALTO");

        CuidadorDTO cuidadorDTO = new CuidadorDTO();
        for (Cuidador cuidador : paciente.cuidadores) {
            cuidadorDTO.setNomeCuidador(cuidador.nomeCuidador);
            cuidadorDTO.setTelefoneCuidador(cuidador.telefoneCuidador);
        }
        pacienteDTO.setCuidador(cuidadorDTO);

        ConsultaDTO consultaDTO = consultaService.buscaProximaConsultaPorPaciente(idPaciente);
        pacienteDTO.setProximaConsulta(consultaDTO);

        List<InteracaoConsultaDTO> historicoConsultas = consultaService.buscarHistoricoConulstasPorPaciente(idPaciente);
        List<InteracaoEquipeDTO> historicoEquipe = anotacaoManualService.buscarHistoricoEquipePorPaciente(idPaciente);
        List<InteracaoSistemaDTO> historicoSistema = interacaoAutomatizadaService.buscarHistoricoSistemaPorPaciente(idPaciente);

        List<LinhaDoTempoDTO> linhaDoTempoDTO = new ArrayList<>();
        linhaDoTempoDTO.addAll(historicoConsultas);
        linhaDoTempoDTO.addAll(historicoEquipe);
        linhaDoTempoDTO.addAll(historicoSistema);
        linhaDoTempoDTO.sort((a, b) -> b.getData().compareTo(a.getData())); // Ordena por data decrescente

        pacienteDTO.setLinhaDoTempo(linhaDoTempoDTO);
        return pacienteDTO;
    }
}
