package br.com.healthtech.imrea.alerta.service;

import br.com.healthtech.imrea.agendamento.domain.Consulta;
import br.com.healthtech.imrea.agendamento.service.ConsultaService;
import br.com.healthtech.imrea.alerta.domain.AlertaSistema;
import br.com.healthtech.imrea.alerta.dto.AlertaDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@ApplicationScoped
public class AlertaSistemaService {

    @Inject
    ConsultaService consultaService;

    public List<AlertaDTO> obterAlertasHoje() {
        List<Consulta> consultasDeHoje = consultaService.buscarConsultasMarcadasHoje();

        if(consultasDeHoje.isEmpty())
            throw new IllegalArgumentException("Não há consultas marcadas hoje");

        List<AlertaDTO> alertas = new ArrayList<>();

        for (Consulta consulta : consultasDeHoje){
            AlertaDTO alertaDTO = new AlertaDTO();
            alertaDTO.setIdPaciente(String.valueOf(consulta.paciente.idPaciente));
            alertaDTO.setNomePaciente(consulta.paciente.nomePaciente);
            alertaDTO.setTelefonePaciente(consulta.paciente.telefonePaciente);
            alertaDTO.setScoreDeRisco(consulta.paciente.scoreDeRisco);
            alertaDTO.setIdConsulta(String.valueOf(consulta.idConsulta));
            alertaDTO.setNomeMedico(consulta.profissional.nomeProfissional);
            alertaDTO.setEspecialidadeConsulta(consulta.profissional.especialidadeProfissional);
            alertaDTO.setHoraConsulta(consulta.dataAgenda.toLocalTime().toString());

            if (alertaDTO.getScoreDeRisco() < 40)
                alertaDTO.setNivelDeRisco(AlertaDTO.NivelRisco.BAIXO);
            else if (alertaDTO.getScoreDeRisco() >= 40 && alertaDTO.getScoreDeRisco() < 75)
                alertaDTO.setNivelDeRisco(AlertaDTO.NivelRisco.MEDIO);
            else if (alertaDTO.getScoreDeRisco() >= 75)
                alertaDTO.setNivelDeRisco(AlertaDTO.NivelRisco.ALTO);

            buscarUltimoAlertaServiceAtivo(alertaDTO);
            alertas.add(alertaDTO);
        }
        orderarListaDeAlertas(alertas);
        return alertas;
    }

    public void buscarUltimoAlertaServiceAtivo(AlertaDTO alertaDTO){
        AlertaSistema alertaSistema = AlertaSistema.find("paciente.idPaciente = ?1 and consulta.idConsulta = ?2 and statusAlerta != 'RESOLVIDO' order by dataHoraAcao desc",
                Long.valueOf(alertaDTO.getIdPaciente()), Long.valueOf(alertaDTO.getIdConsulta())).firstResult();

        if (alertaSistema != null) {
            alertaDTO.setStatusAlerta(AlertaDTO.StatusAlerta.valueOf(alertaSistema.statusAlerta));
            alertaDTO.setPrioridadeAlerta(AlertaDTO.PrioridadeAlerta.valueOf(alertaSistema.prioridadeAlerta));
        } else {
            alertaDTO.setStatusAlerta(AlertaDTO.StatusAlerta.NOVO);
            alertaDTO.setPrioridadeAlerta(AlertaDTO.PrioridadeAlerta.BAIXA);
        }
    }

    private int getPrioridadeValue(AlertaDTO.PrioridadeAlerta prioridade) {
        if (prioridade == AlertaDTO.PrioridadeAlerta.ALTA) return 3;
        if (prioridade == AlertaDTO.PrioridadeAlerta.MEDIA) return 2;
        return 1;
    }

    private void orderarListaDeAlertas(List<AlertaDTO> alertas){
        alertas.sort(Comparator
                .comparing(
                        (AlertaDTO alerta) -> getPrioridadeValue(alerta.getPrioridadeAlerta()),
                        Comparator.reverseOrder()
                )
                .thenComparing(
                        AlertaDTO::getScoreDeRisco,
                        Comparator.reverseOrder()
                ));
    }
}
