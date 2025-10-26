package br.com.healthtech.imrea.agendamento.service;

import br.com.healthtech.imrea.agendamento.domain.Consulta;
import br.com.healthtech.imrea.agendamento.domain.Profissional;
import br.com.healthtech.imrea.agendamento.domain.RegistroAgendamento;
import br.com.healthtech.imrea.agendamento.domain.UploadLog;
import br.com.healthtech.imrea.paciente.domain.Cuidador;
import br.com.healthtech.imrea.paciente.domain.Paciente;
import br.com.healthtech.imrea.paciente.service.CuidadorService;
import br.com.healthtech.imrea.paciente.service.PacienteService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ApplicationScoped
public class AgendamentoMapper {

    @Inject
    PacienteService pacienteService;

    @Inject
    ProfissionalService profissionalService;

    @Inject
    ConsultaService consultaService;

    @Inject
    CuidadorService cuidadorService;

    @Inject
    CepService cepService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public Paciente salvarInformacoesPaciente(RegistroAgendamento registro) {
        Paciente paciente = new Paciente(
                registro.getNomePaciente(),
                registro.getNumeroPaciente(),
                LocalDate.parse(registro.getDataNascimentoPaciente(), DATE_FORMATTER)
        );

        Cuidador cuidador = new Cuidador(registro.getNomeAcompanhante(), registro.getNumeroAcompanhante());
        paciente.getCuidadores().add(cuidadorService.buscarOuCriarCuidador(cuidador));

        paciente.setBairroPaciente(cepService.obterBairroPaciente(registro.getCep()));

        return pacienteService.buscarOuCriarPaciente(paciente);
    }

    public Profissional salvarInformacoesProfissional(RegistroAgendamento registro) {
        Profissional profissional = new Profissional(registro.getNomeMedico(), registro.getEspecialidade());
        return profissionalService.buscarOuCriarMedico(profissional);
    }

    public Consulta salvarInformacoesConsulta(RegistroAgendamento registro, Paciente paciente, Profissional profissional, UploadLog uploadLog){
        LocalDateTime dataAgenda = LocalDateTime.parse(registro.getDataAgendamento() + " " + registro.getHoraAgendamento(), DATE_TIME_FORMATTER);

        Consulta consulta = new Consulta(
                paciente,
                profissional,
                dataAgenda,
                registro.getLinkConsulta(),
                registro.getCodigoConsulta(),
                registro.getObsAgendamento()
        );

        consulta.setUploadLog(uploadLog);

        return consultaService.buscarOuCriarConsulta(consulta);
    }
}
