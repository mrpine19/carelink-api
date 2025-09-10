package br.com.healthtech.imrea.agendamento.service;

import br.com.healthtech.imrea.agendamento.domain.Consulta;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@ApplicationScoped
public class ConsultaService {

    private static final Logger logger = LoggerFactory.getLogger(ConsultaService.class);

    @Transactional
    public void buscarOuCriarConsulta(Consulta consulta){
        if(consulta.dataAgenda == null || consulta.linkConsulta == null){
            throw new IllegalArgumentException("Infomações de agendamento inválidas");
        }

        Consulta consultaExistente = Consulta.find("dataAgenda = ?1 and paciente = ?2 and profissional = ?3", consulta.dataAgenda,
                                                    consulta.paciente, consulta.profissional).firstResult();

        if(consultaExistente == null){
            consulta.dtCriacaoConsulta = LocalDateTime.now();
            consulta.persist();
            logger.info("Agendamento marcado para paciente {}, na data {}", consulta.paciente.nomePaciente,
                    consulta.dataAgenda);
        }
        else {
            logger.info("O paciente {} já possui um agendamento para a data {}", consulta.paciente.nomePaciente,
                    consulta.dataAgenda);
        }
    }

    public List<Consulta> buscarConsultasMarcadasDiaSeguinte() {
        LocalDate amanha = LocalDate.now().plusDays(1);
        LocalDateTime inicioDoDia = amanha.atStartOfDay();
        LocalDateTime fimDoDia = amanha.atTime(LocalTime.MAX);

        return Consulta.find("dataAgenda >= ?1 and dataAgenda <= ?2", inicioDoDia, fimDoDia).list();
    }

}
