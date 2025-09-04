package br.com.healthtech.imrea.paciente.service;

import br.com.healthtech.imrea.paciente.domain.Paciente;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@ApplicationScoped
public class PacienteService {

    private static final Logger logger = LoggerFactory.getLogger(PacienteService.class);

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

}
