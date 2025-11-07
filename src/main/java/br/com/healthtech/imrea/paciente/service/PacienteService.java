package br.com.healthtech.imrea.paciente.service;

import br.com.healthtech.imrea.consulta.service.ConsultaService;
import br.com.healthtech.imrea.interacao.dto.InteracaoConsultaDTO;
import br.com.healthtech.imrea.interacao.dto.InteracaoEquipeDTO;
import br.com.healthtech.imrea.interacao.dto.InteracaoSistemaDTO;
import br.com.healthtech.imrea.interacao.dto.LinhaDoTempoDTO;
import br.com.healthtech.imrea.interacao.service.AnotacaoManualService;
import br.com.healthtech.imrea.interacao.service.InteracaoAutomatizadaService;
import br.com.healthtech.imrea.paciente.domain.Cuidador;
import br.com.healthtech.imrea.paciente.domain.Paciente;
import br.com.healthtech.imrea.paciente.dto.ConsultaPacienteDTO;
import br.com.healthtech.imrea.paciente.dto.CuidadorDTO;
import br.com.healthtech.imrea.paciente.dto.PacienteDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        if (paciente.getNomePaciente() == null || paciente.getNomePaciente().isEmpty()){
            throw new IllegalArgumentException("Nome do paciente inválido");
        }
        Paciente pacienteExistente = Paciente.find("nomePaciente = ?1 and telefonePaciente = ?2", paciente.getNomePaciente(), paciente.getTelefonePaciente()).firstResult();
        if (pacienteExistente == null){
            paciente.setDtCriacaoPaciente(LocalDateTime.now());
            paciente.persist();
            logger.info("Paciente {} salvo com sucesso!", paciente.getNomePaciente());
            return paciente;
        }else {
            logger.info("Paciente {} já existe!", paciente.getNomePaciente());
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
        pacienteDTO.setIdPaciente(paciente.getIdPaciente());
        pacienteDTO.setNomePaciente(paciente.getNomePaciente());
        pacienteDTO.setTelefonePaciente(paciente.getTelefonePaciente());
        pacienteDTO.setBairroPaciente(paciente.getBairroPaciente());
        pacienteDTO.setDataNascimentoPaciente(paciente.getDataNascimentoPaciente().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        pacienteDTO.setScoreDeRisco(paciente.getScoreDeRisco());

        if (pacienteDTO.getScoreDeRisco() <= 400)
            pacienteDTO.setNivelDeRisco("BAIXO");
        if (pacienteDTO.getScoreDeRisco() > 400 && pacienteDTO.getScoreDeRisco() <= 700)
            pacienteDTO.setNivelDeRisco("MEDIO");
        if (pacienteDTO.getScoreDeRisco() > 700 && pacienteDTO.getScoreDeRisco() <= 850)
            pacienteDTO.setNivelDeRisco("ALTO");
        if (pacienteDTO.getScoreDeRisco() > 850)
            pacienteDTO.setNivelDeRisco("CRITICO");

        CuidadorDTO cuidadorDTO = new CuidadorDTO();
        for (Cuidador cuidador : paciente.getCuidadores()) {
            cuidadorDTO.setNomeCuidador(cuidador.getNomeCuidador());
            cuidadorDTO.setTelefoneCuidador(cuidador.getTelefoneCuidador());
        }
        pacienteDTO.setCuidador(cuidadorDTO);

        ConsultaPacienteDTO consultaPacienteDTO = consultaService.buscaProximaConsultaPorPaciente(idPaciente);
        pacienteDTO.setProximaConsulta(consultaPacienteDTO);

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

    public Paciente buscarPacientePorTelefone(String telefonePaciente) {
        return Paciente.find("telefonePaciente = ?1", telefonePaciente).firstResult();
    }
}
