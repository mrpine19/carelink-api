package br.com.healthtech.imrea.consulta.domain;

import br.com.healthtech.imrea.agendamento.domain.Profissional;
import br.com.healthtech.imrea.agendamento.domain.UploadLog;
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
    private Long idConsulta;

    @ManyToOne
    @JoinColumn(name = "id_paciente")
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name="id_profissional")
    private Profissional profissional;

    @ManyToOne
    @JoinColumn(name="id_especialidade")
    private Especialidade especialidade;

    @ManyToOne
    @JoinColumn(name="id_upload")
    private UploadLog uploadLog;

    @Column(name="data_agenda")
    private LocalDateTime dataAgenda;

    @Column(name="link_acesso")
    private String linkConsulta;

    @Column(name="codigo_acesso")
    private String codigoConsulta;

    @Column(name="obs_agendamento")
    private String obsConsulta;

    @Column(name="status_consulta")
    private String statusConsulta;

    @Column(name="data_registro_status")
    private LocalDateTime dataRegistroStatus;

    @Column(name="dt_criacao")
    private LocalDateTime dtCriacaoConsulta;

    public Consulta() {
    }

    public Consulta(Paciente paciente, Profissional profissional, LocalDateTime dataAgenda, String linkConsulta, String codigoConsulta, String obsConsulta) {
        this.paciente = paciente;
        this.profissional = profissional;
        this.dataAgenda = dataAgenda;
        this.linkConsulta = linkConsulta;
        this.codigoConsulta = codigoConsulta;
        this.obsConsulta = obsConsulta;
    }

    public Long getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(Long idConsulta) {
        this.idConsulta = idConsulta;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Profissional getProfissional() {
        return profissional;
    }

    public void setProfissional(Profissional profissional) {
        this.profissional = profissional;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
    }

    public UploadLog getUploadLog() {
        return uploadLog;
    }

    public void setUploadLog(UploadLog uploadLog) {
        this.uploadLog = uploadLog;
    }

    public LocalDateTime getDataAgenda() {
        return dataAgenda;
    }

    public void setDataAgenda(LocalDateTime dataAgenda) {
        this.dataAgenda = dataAgenda;
    }

    public String getLinkConsulta() {
        return linkConsulta;
    }

    public void setLinkConsulta(String linkConsulta) {
        this.linkConsulta = linkConsulta;
    }

    public String getCodigoConsulta() {
        return codigoConsulta;
    }

    public void setCodigoConsulta(String codigoConsulta) {
        this.codigoConsulta = codigoConsulta;
    }

    public String getObsConsulta() {
        return obsConsulta;
    }

    public void setObsConsulta(String obsConsulta) {
        this.obsConsulta = obsConsulta;
    }

    public String getStatusConsulta() {
        return statusConsulta;
    }

    public void setStatusConsulta(String statusConsulta) {
        this.statusConsulta = statusConsulta;
    }

    public LocalDateTime getDataRegistroStatus() {
        return dataRegistroStatus;
    }

    public void setDataRegistroStatus(LocalDateTime dataRegistroStatus) {
        this.dataRegistroStatus = dataRegistroStatus;
    }

    public LocalDateTime getDtCriacaoConsulta() {
        return dtCriacaoConsulta;
    }

    public void setDtCriacaoConsulta(LocalDateTime dtCriacaoConsulta) {
        this.dtCriacaoConsulta = dtCriacaoConsulta;
    }
}
