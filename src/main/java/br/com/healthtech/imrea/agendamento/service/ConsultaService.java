package br.com.healthtech.imrea.agendamento.service;

import br.com.healthtech.imrea.agendamento.domain.Consulta;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@ApplicationScoped
public class ConsultaService {

    private static final Logger logger = LoggerFactory.getLogger(ConsultaService.class);

    @Transactional
    public void buscarOuCriarConsulta(Consulta consulta){
        if(consulta.dataAgenda == null || consulta.horaAgenda == null || consulta.linkConsulta == null){
            throw new IllegalArgumentException("Infomações de agendamento inválidas");
        }

        Consulta consultaExistente = Consulta.find("dataAgenda = ?1 and horaAgenda = ?2 and paciente = ?3 and profissional = ?4", consulta.dataAgenda, consulta.horaAgenda,
                                                    consulta.paciente, consulta.profissional).firstResult();

        if(consultaExistente == null){
            consulta.dtCriacaoConsulta = LocalDateTime.now();
            consulta.persist();
            logger.info("Agendamento marcado para paciente {}, na data {} às {}", consulta.paciente.nomePaciente,
                    consulta.dataAgenda, consulta.horaAgenda);
        }
        else {
            logger.info("O paciente {} já possui um agendamento para a data {} às {}", consulta.paciente.nomePaciente,
                    consulta.dataAgenda, consulta.horaAgenda);
        }
    }

}
