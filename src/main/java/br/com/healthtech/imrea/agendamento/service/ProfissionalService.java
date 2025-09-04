package br.com.healthtech.imrea.agendamento.service;

import br.com.healthtech.imrea.agendamento.domain.Profissional;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@ApplicationScoped
public class ProfissionalService {

    private static final Logger logger = LoggerFactory.getLogger(ProfissionalService.class);

    @Transactional
    public Profissional buscarOuCriarMedico(Profissional profissional) {
        if (profissional.nomeProfissional == null || profissional.nomeProfissional.isEmpty()) {
            throw new IllegalArgumentException("Nome do médico inválido!");
        }

        Profissional profissionalExistente = Profissional.find("nomeProfissional = ?1", profissional.nomeProfissional).firstResult();

        if (profissionalExistente == null) {
            profissional.dtCriacaoProfissional = LocalDateTime.now();
            Profissional.persist(profissional);
            logger.info("Medico {} cadastrado com sucesso!", profissional.nomeProfissional);
            return profissional;
        } else {
            logger.info("Medico {} já cadastrado!", profissional.nomeProfissional);
            return profissionalExistente;
        }
    }

}
