package br.com.healthtech.imrea.agendamento.domain;

import br.com.healthtech.imrea.paciente.domain.Paciente;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name="TB_CAR_CONSULTA")
public class Agendamento extends PanacheEntity {

    @ManyToOne
    @JoinColumn(name = "id_paciente")
    public Paciente paciente;

    @Column(name="data_agenda")
    public String dataAgenda;

    @Column(name="hora_agenda")
    public String horaAgenda;

    @Column(name="link_acesso")
    public String linkConsulta;

    @Column(name="codigo_acesso")
    public int codigoConsulta;

    @Column(name="obs_agendamento")
    public String obsAgendamento;

    public Agendamento() {
    }

    public Agendamento(Paciente paciente, String dataAgenda, String horaAgenda, String linkConsulta, int codigoConsulta, String obsAgendamento) {
        this.paciente = paciente;
        this.dataAgenda = dataAgenda;
        this.horaAgenda = horaAgenda;
        this.linkConsulta = linkConsulta;
        this.codigoConsulta = codigoConsulta;
        this.obsAgendamento = obsAgendamento;
    }
}
