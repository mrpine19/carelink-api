package br.com.healthtech.imrea.agendamento.service;

import br.com.healthtech.imrea.agendamento.domain.Consulta;
import br.com.healthtech.imrea.interacao.dto.InteracaoConsultaDTO;
import br.com.healthtech.imrea.paciente.dto.ConsultaDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ConsultaService {

    private static final Logger logger = LoggerFactory.getLogger(ConsultaService.class);

    @Transactional
    public Consulta buscarOuCriarConsulta(Consulta consulta){
        if(consulta.dataAgenda == null || consulta.linkConsulta == null){
            throw new IllegalArgumentException("Infomações de agendamento inválidas");
        }

        Consulta consultaExistente = Consulta.find("dataAgenda = ?1 and paciente = ?2 and profissional = ?3", consulta.dataAgenda,
                                                    consulta.paciente, consulta.profissional).firstResult();

        if(consultaExistente == null){
            consulta.statusConsulta = "AGENDADO";
            consulta.dataRegistroStatus = LocalDateTime.now();
            consulta.dtCriacaoConsulta = LocalDateTime.now();
            consulta.persist();
            logger.info("Agendamento marcado para paciente {}, na data {}", consulta.paciente.nomePaciente,
                    consulta.dataAgenda);
            return consulta;
        }
        else {
            logger.info("O paciente {} já possui um agendamento para a data {}", consulta.paciente.nomePaciente,
                    consulta.dataAgenda);
            return consultaExistente;
        }
    }

    public List<Consulta> buscarConsultasMarcadasDiaSeguinte() {
        LocalDate amanha = LocalDate.now().plusDays(1);
        LocalDateTime inicioDoDia = amanha.atStartOfDay();
        LocalDateTime fimDoDia = amanha.atTime(LocalTime.MAX);

        return Consulta.find("dataAgenda >= ?1 and dataAgenda <= ?2", inicioDoDia, fimDoDia).list();
    }

    public List<Consulta> buscarConsultasMarcadasHoje(){
        LocalDate hoje = LocalDate.now();
        return Consulta.find("dataAgenda >= ?1 and dataAgenda <= ?2", hoje.atStartOfDay(), hoje.atTime(LocalTime.MAX)).list();
    }

    public List<Consulta> buscarConsultasMarcadasProximaHora() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime proximaHora = LocalDateTime.now().plusHours(1);

        return Consulta.find("dataAgenda >= ?1 and dataAgenda <= ?2", agora, proximaHora).list();
    }

    public ConsultaDTO buscaProximaConsultaPorPaciente(Long idPaciente) {

        LocalDateTime inicioDoDiaDeHoje = LocalDate.now().atStartOfDay();

        Consulta consulta = Consulta.find(
                "paciente.idPaciente = ?1 and dataAgenda >= ?2 order by dataAgenda asc",
                idPaciente, inicioDoDiaDeHoje
        ).firstResult();

        if (consulta == null) {
            return null;
        }

        ConsultaDTO consultaDTO = new ConsultaDTO();
        consultaDTO.setDataConsulta(consulta.dataAgenda.toLocalDate().toString());
        consultaDTO.setHoraConsulta(consulta.dataAgenda.toLocalTime().toString());
        consultaDTO.setNomeMedico(consulta.profissional != null ? consulta.profissional.nomeProfissional : null);
        consultaDTO.setEspecialidadeConsulta(consulta.profissional != null ? consulta.profissional.especialidadeProfissional : null);
        return consultaDTO;
    }

    public List<InteracaoConsultaDTO> buscarHistoricoConulstasPorPaciente(Long idPaciente) {
        List<Consulta> consultas = Consulta.find("paciente.idPaciente = ?1 order by dataAgenda desc", idPaciente).list();
        List<InteracaoConsultaDTO> historico = new ArrayList<>();

        for (Consulta consulta : consultas) {
            InteracaoConsultaDTO consultaDTO = new InteracaoConsultaDTO();
            consultaDTO.setTipo("CONSULTA");
            consultaDTO.setData(consulta.dataAgenda.toLocalDate().toString());
            consultaDTO.setHora(consulta.dataAgenda.toLocalTime().toString());
            consultaDTO.setStatus(consulta.statusConsulta);
            consultaDTO.setModalidade("Telemedicina");
            consultaDTO.setProfissional(consulta.profissional.nomeProfissional);
            consultaDTO.setEspecialidade(consulta.profissional.especialidadeProfissional);
            historico.add(consultaDTO);
        }
        return historico;
    }
}
