package br.com.healthtech.imrea.agendamento.domain;

import br.com.healthtech.imrea.paciente.domain.Paciente;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name="TB_CAR_CONSULTA")
public class Consulta extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="id_consulta")
    private Long idConsulta;

    @ManyToOne
    @JoinColumn(name = "id_paciente")
    public Paciente paciente;

    @ManyToOne
    @JoinColumn(name="id_profissional")
    public Profissional profissional;

    @Column(name="data_agenda")
    public Date dataAgenda;

    @Column(name="hora_agenda")
    public String horaAgenda;

    @Column(name="link_acesso")
    public String linkConsulta;

    @Column(name="codigo_acesso")
    public String codigoConsulta;

    @Column(name="obs_agendamento")
    public String obsConsulta;

    @Column(name="data_criacao")
    public LocalDateTime dtCriacaoConsulta;

    public Consulta() {
    }

    public Consulta(Paciente paciente, Profissional profissional, Date dataAgenda, String horaAgenda, String linkConsulta, String codigoConsulta, String obsConsulta) {
        this.paciente = paciente;
        this.profissional = profissional;
        this.dataAgenda = dataAgenda;
        this.horaAgenda = horaAgenda;
        this.linkConsulta = linkConsulta;
        this.codigoConsulta = codigoConsulta;
        this.obsConsulta = obsConsulta;
    }
}
