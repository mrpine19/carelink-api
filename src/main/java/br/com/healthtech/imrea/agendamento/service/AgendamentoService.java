package br.com.healthtech.imrea.agendamento.service;

import br.com.healthtech.imrea.agendamento.domain.Agendamento;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class AgendamentoService {

    private static final Logger logger = LoggerFactory.getLogger(AgendamentoService.class);

    @Transactional
    public void save(Agendamento agendamento){
        if(agendamento.dataAgenda == null || agendamento.horaAgenda == null || agendamento.linkConsulta == null){
            throw new IllegalArgumentException("Infomações de agendamento inválidas");
        }

        if(buscarConsultaPorDataEHora(agendamento) == null){
            agendamento.persist();
            logger.info("Agendamento marcado para paciente {}, na data {} às {}", agendamento.paciente.nomePaciente,
                    agendamento.dataAgenda, agendamento.horaAgenda);
        }
        else {
            logger.info("O paciente {} já possui um agendamento para a data {} às {}", agendamento.paciente.nomePaciente,
                    agendamento.dataAgenda, agendamento.horaAgenda);
        }
    }

    public Agendamento buscarConsultaPorDataEHora(Agendamento agendamento){
        return Agendamento.find("dataAgenda = ?1 and horaAgenda = ?2", agendamento.dataAgenda, agendamento.horaAgenda).firstResult();
    }
}
