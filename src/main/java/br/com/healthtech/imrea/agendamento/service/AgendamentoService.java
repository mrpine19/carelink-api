package br.com.healthtech.imrea.agendamento.service;

import br.com.healthtech.imrea.agendamento.domain.Agendamento;
import br.com.healthtech.imrea.paciente.service.PacienteService;
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

        agendamento.persist();
    }
}
