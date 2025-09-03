package br.com.healthtech.imrea.paciente.service;

import br.com.healthtech.imrea.agendamento.service.UploadPlanilhaService;
import br.com.healthtech.imrea.paciente.domain.Paciente;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@ApplicationScoped
public class PacienteService {

    private static final Logger logger = LoggerFactory.getLogger(UploadPlanilhaService.class);

    @Transactional
    public void save(Paciente paciente){
        if (paciente.nomePaciente == null || paciente.nomePaciente.isEmpty()){
            throw new IllegalArgumentException("Nome do paciente inválido");
        }
        if (buscarPorNomeETelefone(paciente) == null){
            paciente.persist();
            logger.info("Paciente {} salvo com sucesso!", paciente.nomePaciente);
        }else {
            logger.info("Paciente {} já existe!", paciente.nomePaciente);
        }
    }

    public Paciente buscarPorNomeETelefone(Paciente paciente){
        return Paciente.find("nomePaciente = ?1 and telefonePaciente = ?2", paciente.nomePaciente, paciente.telefonePaciente).firstResult();
    }
}
