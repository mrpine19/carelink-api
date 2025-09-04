package br.com.healthtech.imrea.agendamento.domain;

import br.com.healthtech.imrea.paciente.domain.Paciente;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="TB_CAR_CONSULTA")
public class Consulta extends PanacheEntity {

    @ManyToOne
    @JoinColumn(name = "id_paciente")
    public Paciente paciente;

    @ManyToOne
    @JoinColumn(name="id_profissional")
    public Profissional profissional;

    @Column(name="data_agenda")
    public String dataAgenda;

    @Column(name="hora_agenda")
    public String horaAgenda;

    @Column(name="link_acesso")
    public String linkConsulta;

    @Column(name="codigo_acesso")
    public int codigoConsulta;

    @Column(name="obs_agendamento")
    public String obsConsulta;

    @Column(name="dt_criacao_consulta")
    public LocalDateTime dtCriacaoConsulta;

    public Consulta() {
    }

    public Consulta(Paciente paciente, Profissional profissional, String dataAgenda, String horaAgenda, String linkConsulta, int codigoConsulta, String obsConsulta) {
        this.paciente = paciente;
        this.profissional = profissional;
        this.dataAgenda = dataAgenda;
        this.horaAgenda = horaAgenda;
        this.linkConsulta = linkConsulta;
        this.codigoConsulta = codigoConsulta;
        this.obsConsulta = obsConsulta;
    }
}
