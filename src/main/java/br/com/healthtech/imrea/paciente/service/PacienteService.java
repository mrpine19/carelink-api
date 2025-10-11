package br.com.healthtech.imrea.paciente.service;

import br.com.healthtech.imrea.paciente.domain.Paciente;
import br.com.healthtech.imrea.paciente.dto.PacienteDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@ApplicationScoped
public class PacienteService {

    private static final Logger logger = LoggerFactory.getLogger(PacienteService.class);

    private final CuidadorService cuidadorService;

    public PacienteService(CuidadorService cuidadorService) {
        this.cuidadorService = cuidadorService;
    }

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
    public PacienteDTO buscarPacientePorId(Long idPaciente) {
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
        pacienteDTO.setCuidador(cuidadorService.buscarCuidadoresPorPaciente(idPaciente));
        return pacienteDTO;
    }
}
