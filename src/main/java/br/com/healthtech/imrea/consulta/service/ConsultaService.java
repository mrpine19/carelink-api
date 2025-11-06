package br.com.healthtech.imrea.consulta.service;

import br.com.healthtech.imrea.consulta.domain.Consulta;
import br.com.healthtech.imrea.consulta.dto.ConsultaDTO;
import br.com.healthtech.imrea.consulta.dto.ConsultaUpdateDTO;
import br.com.healthtech.imrea.interacao.dto.InteracaoConsultaDTO;
import br.com.healthtech.imrea.paciente.domain.Paciente;
import br.com.healthtech.imrea.paciente.dto.ConsultaPacienteDTO;
import br.com.healthtech.imrea.paciente.service.PacienteService;
import jakarta.enterprise.context.ApplicationScoped;
import br.com.healthtech.imrea.paciente.domain.Cuidador;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ConsultaService {

    @Inject
    PacienteService pacienteService;

    private static final Logger logger = LoggerFactory.getLogger(ConsultaService.class);

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Transactional
    public Consulta buscarOuCriarConsulta(Consulta consulta) {
        if (consulta.getDataAgenda() == null)
            throw new IllegalArgumentException("Infomações de agendamento inválidas");

        Consulta consultaExistente = Consulta.find("dataAgenda = ?1 and paciente = ?2 and profissional = ?3", consulta.getDataAgenda(),
                consulta.getPaciente(), consulta.getProfissional()).firstResult();

        if (consultaExistente == null) {
            consulta.setStatusConsulta("AGENDADA");
            consulta.setDataRegistroStatus(LocalDateTime.now());
            consulta.setDtCriacaoConsulta(LocalDateTime.now());
            consulta.persist();
            logger.info("Agendamento marcado para paciente {}, na data {}", consulta.getPaciente().getNomePaciente(),
                    consulta.getDataAgenda());
            return consulta;
        } else {
            logger.info("O paciente {} já possui um agendamento para a data {}", consulta.getPaciente().getNomePaciente(),
                    consulta.getDataAgenda());
            return consultaExistente;
        }
    }

    public List<Consulta> buscarConsultasMarcadasDiaSeguinte() {
        LocalDate amanha = LocalDate.now().plusDays(1);
        LocalDateTime inicioDoDia = amanha.atStartOfDay();
        LocalDateTime fimDoDia = amanha.atTime(LocalTime.MAX);

        return Consulta.find("dataAgenda >= ?1 and dataAgenda <= ?2", inicioDoDia, fimDoDia).list();
    }

    public List<Consulta> buscarConsultasMarcadasHoje() {
        LocalDate hoje = LocalDate.now();
        return Consulta.find("dataAgenda >= ?1 and dataAgenda <= ?2", hoje.atStartOfDay(), hoje.atTime(LocalTime.MAX)).list();
    }

    public List<Consulta> buscarConsultasMarcadasProximaHora() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime proximaHora = LocalDateTime.now().plusHours(1);

        return Consulta.find("dataAgenda >= ?1 and dataAgenda <= ?2", agora, proximaHora).list();
    }

    public ConsultaPacienteDTO buscaProximaConsultaPorPaciente(Long idPaciente) {

        LocalDateTime inicioDoDiaDeHoje = LocalDate.now().atStartOfDay();

        Consulta consulta = Consulta.find(
                "paciente.idPaciente = ?1 and dataAgenda >= ?2 order by dataAgenda asc",
                idPaciente, inicioDoDiaDeHoje
        ).firstResult();

        if (consulta == null) {
            return null;
        }

        ConsultaPacienteDTO consultaPacienteDTO = new ConsultaPacienteDTO();
        consultaPacienteDTO.setDataConsulta(consulta.getDataAgenda().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        consultaPacienteDTO.setHoraConsulta(consulta.getDataAgenda().toLocalTime().toString());
        consultaPacienteDTO.setNomeMedico(consulta.getProfissional() != null ? consulta.getProfissional().getNomeProfissional() : null);
        consultaPacienteDTO.setEspecialidadeConsulta(consulta.getProfissional() != null ? consulta.getEspecialidade().getNomeEspecialidade() : null);
        return consultaPacienteDTO;
    }

    public List<Consulta> buscarConsultasPorPaciente(Long idPaciente) {
        return Consulta.find("paciente.idPaciente = ?1 order by dataAgenda desc", idPaciente).list();
    }

    public Consulta buscarConsultaMaisRecenteRealizada(Long idPaciente) {
        return Consulta.find("paciente.idPaciente = ?1 AND statusConsulta = 'REALIZADA' order by dataAgenda desc", idPaciente).firstResult();
    }

    public List<Consulta> buscarConsultasPassadasParaTaxa(Long idPaciente) {
        return Consulta.find("paciente.idPaciente = ?1 AND NOT statusConsulta = 'CANCELADA' order by dataAgenda desc", idPaciente).list();
    }

    public List<InteracaoConsultaDTO> buscarHistoricoConulstasPorPaciente(Long idPaciente) {
        List<Consulta> consultas = buscarConsultasPorPaciente(idPaciente);
        List<InteracaoConsultaDTO> historico = new ArrayList<>();

        for (Consulta consulta : consultas) {
            InteracaoConsultaDTO consultaDTO = new InteracaoConsultaDTO();
            consultaDTO.setTipo("CONSULTA");
            consultaDTO.setData(consulta.getDataAgenda().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            consultaDTO.setHora(consulta.getDataAgenda().toLocalTime().toString());
            consultaDTO.setStatus(consulta.getStatusConsulta());
            consultaDTO.setModalidade("Telemedicina");
            consultaDTO.setProfissional(consulta.getProfissional().getNomeProfissional());
            consultaDTO.setEspecialidade(consulta.getEspecialidade().getNomeEspecialidade());
            historico.add(consultaDTO);
        }
        return historico;
    }

    public List<ConsultaDTO> buscarConsultasPorPeriodo(String dataInicio, String dataFim) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDateTime inicio = LocalDate.parse(dataInicio, formatter).atStartOfDay();
        LocalDateTime fim = LocalDate.parse(dataFim, formatter).minusDays(1).atTime(LocalTime.MAX);

        List<Consulta> consultas = Consulta.find("dataAgenda >= ?1 and dataAgenda <= ?2 order by dataAgenda", inicio, fim).list();
        List<ConsultaDTO> dtos = new ArrayList<>();

        for (Consulta consulta : consultas) {
            ConsultaDTO consultaDTO = new ConsultaDTO();
            consultaDTO.setIdConsulta(consulta.getIdConsulta().intValue());
            consultaDTO.setDataConsulta(consulta.getDataAgenda().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            consultaDTO.setHoraConsulta(consulta.getDataAgenda().toLocalTime().toString());
            consultaDTO.setNomePaciente(consulta.getPaciente().getNomePaciente());
            consultaDTO.setTelefonePaciente(consulta.getPaciente().getTelefonePaciente());

            if (!consulta.getPaciente().getCuidadores().isEmpty()) {
                Cuidador primeiroCuidador = consulta.getPaciente().getCuidadores().iterator().next();
                consultaDTO.setNomeCuidador(primeiroCuidador.getNomeCuidador());
                consultaDTO.setTelefoneCuidador(primeiroCuidador.getTelefoneCuidador());
            }

            consultaDTO.setNomeProfissional(consulta.getProfissional().getNomeProfissional());
            consultaDTO.setEspecialidadeProfissional(consulta.getEspecialidade().getNomeEspecialidade());
            consultaDTO.setStatusConsulta(consulta.getStatusConsulta());
            consultaDTO.setLinkConsulta(consulta.getEspecialidade().getLinkConsultaEspecialidade());
            consultaDTO.setCodigoConsulta(consulta.getCodigoConsulta());
            consultaDTO.setAnotacoes(consulta.getObsConsulta());
            dtos.add(consultaDTO);
        }
        return dtos;
    }

    @Transactional
    public void atualizarConsulta(Long idConsulta, ConsultaUpdateDTO consultaUpdateDTO) {
        Consulta consulta = Consulta.findById(idConsulta);

        if (consulta == null)
            throw new IllegalArgumentException("Consulta com id " + idConsulta + "não encontrada");

        String dataFormatada = converteFormatoDeData(consultaUpdateDTO.getDate());
        LocalDateTime dataAgenda = LocalDateTime.parse(dataFormatada + " " + consultaUpdateDTO.getTime(), DATE_TIME_FORMATTER);

        consulta.setDataAgenda(dataAgenda);
        consulta.setStatusConsulta(consultaUpdateDTO.getStatus());

        if (consulta.getPaciente().getDataPrimeiraConsulta() == null)
            consulta.getPaciente().setDataPrimeiraConsulta(LocalDate.now());
        populaHistoricoDeFaltas(consulta.getPaciente(), consultaUpdateDTO.getStatus());
    }

    public String converteFormatoDeData(String data){
        DateTimeFormatter formatoEntrada = DateTimeFormatter.ISO_LOCAL_DATE;
        DateTimeFormatter formatoSaida = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataConvertida = LocalDate.parse(data, formatoEntrada);
        return dataConvertida.format(formatoSaida);
    }

    public void populaHistoricoDeFaltas(Paciente paciente, String statusConsulta){
        if (statusConsulta.equals("REALIZADA"))
            paciente.setNumeroFaltasConsecutivas(0);
        else if(statusConsulta.equals("PACIENTE NAO COMPARECEU")){
            int faltas = paciente.getNumeroFaltasConsecutivas();
            paciente.setNumeroFaltasConsecutivas(faltas + 1);
        }
    }
}