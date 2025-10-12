package br.com.healthtech.imrea.agendamento.service;

import br.com.healthtech.imrea.agendamento.domain.Consulta;
import br.com.healthtech.imrea.paciente.dto.ConsultaDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.*;
import java.util.Date;
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

    public ConsultaDTO buscaProximaConsultaPorPaciente(Long idPaciente) {
        Instant instantAgora = Instant.now();

        // 2. Converte o Instant para java.util.Date, que é o tipo da sua entidade
        Date dateAgora = Date.from(instantAgora);

        // 3. Usa o Date na query
        Consulta consulta = Consulta.find(
                "paciente.idPaciente = ?1 and dataAgenda >= ?2 order by dataAgenda asc",
                idPaciente, dateAgora // Passamos o Date
        ).firstResult();

        if (consulta == null) {
            return null;
        }

        ConsultaDTO consultaDTO = new ConsultaDTO();
        LocalDateTime ldt = consulta.dataAgenda.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        consultaDTO.setDataConsulta(ldt.toLocalDate().toString());
        consultaDTO.setHoraConsulta(ldt.toLocalTime().toString());
        consultaDTO.setNomeMedico(consulta.profissional != null ? consulta.profissional.nomeProfissional : null);
        consultaDTO.setEspecialidadeConsulta(consulta.profissional != null ? consulta.profissional.especialidadeProfissional : null);
        return consultaDTO;
    }

}
