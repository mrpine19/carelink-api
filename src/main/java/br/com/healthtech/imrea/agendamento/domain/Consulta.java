package br.com.healthtech.imrea.agendamento.domain;

import br.com.healthtech.imrea.paciente.domain.Paciente;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="TB_CAR_CONSULTA")
public class Consulta extends PanacheEntityBase {

    @Id
    @SequenceGenerator(name = "consultaSequence", sequenceName = "TB_CAR_CONSULTA_id_consulta_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "consultaSequence")
    @Column(name="id_consulta")
    public Long idConsulta;

    @ManyToOne
    @JoinColumn(name = "id_paciente")
    public Paciente paciente;

    @ManyToOne
    @JoinColumn(name="id_profissional")
    public Profissional profissional;

    @ManyToOne
    @JoinColumn(name="id_upload")
    public UploadLog uploadLog;

    @Column(name="data_agenda")
    public LocalDateTime dataAgenda;

    @Column(name="link_acesso")
    public String linkConsulta;

    @Column(name="codigo_acesso")
    public String codigoConsulta;

    @Column(name="obs_agendamento")
    public String obsConsulta;

    @Column(name="status_consulta")
    public String statusConsulta;

    @Column(name="data_registro_status")
    public LocalDateTime dataRegistroStatus;

    @Column(name="dt_criacao")
    public LocalDateTime dtCriacaoConsulta;

    public Consulta() {
    }

    public Consulta(Paciente paciente, Profissional profissional, UploadLog uploadLog, LocalDateTime dataAgenda, String linkConsulta, String codigoConsulta, String obsConsulta) {
        this.paciente = paciente;
        this.profissional = profissional;
        this.uploadLog = uploadLog;
        this.dataAgenda = dataAgenda;
        this.linkConsulta = linkConsulta;
        this.codigoConsulta = codigoConsulta;
        this.obsConsulta = obsConsulta;
    }
}
