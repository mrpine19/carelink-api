package br.com.healthtech.imrea.interacao.domain;

import br.com.healthtech.imrea.agendamento.domain.Consulta;
import br.com.healthtech.imrea.paciente.domain.Paciente;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="TB_CAR_INTERACAO_AUTOMATIZADA")
public class InteracaoAutomatizada extends PanacheEntityBase {

    @Id
    @SequenceGenerator(name = "interacaoSequence", sequenceName = "TB_CAR_INTERACAO_AUTOMATIZADA_id_interacao_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "interacaoSequence")
    @Column(name="id_interacao")
    private Long idInteracao;

    @ManyToOne
    @JoinColumn(name="id_consulta")
    private Consulta consulta;

    @ManyToOne
    @JoinColumn(name="id_paciente")
    private Paciente paciente;

    @Column(name="tipo_interacao")
    private String tipoInteracao;

    @Column(name="receptor_tipo")
    private String receptorTipo;

    @Column(name="status_interacao")
    private String statusInteracao;

    @Column(name="detalhes_interacao")
    private String detalhesInteracao;

    @Column(name="data_hora_interacao")
    private LocalDateTime dataHoraInteracao;

    public InteracaoAutomatizada(Consulta consulta, Paciente paciente) {
        this.consulta = consulta;
        this.paciente = paciente;
    }

    public InteracaoAutomatizada() {

    }

    public Long getIdInteracao() {
        return idInteracao;
    }

    public void setIdInteracao(Long idInteracao) {
        this.idInteracao = idInteracao;
    }

    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public String getTipoInteracao() {
        return tipoInteracao;
    }

    public void setTipoInteracao(String tipoInteracao) {
        this.tipoInteracao = tipoInteracao;
    }

    public String getReceptorTipo() {
        return receptorTipo;
    }

    public void setReceptorTipo(String receptorTipo) {
        this.receptorTipo = receptorTipo;
    }

    public String getStatusInteracao() {
        return statusInteracao;
    }

    public void setStatusInteracao(String statusInteracao) {
        this.statusInteracao = statusInteracao;
    }

    public String getDetalhesInteracao() {
        return detalhesInteracao;
    }

    public void setDetalhesInteracao(String detalhesInteracao) {
        this.detalhesInteracao = detalhesInteracao;
    }

    public LocalDateTime getDataHoraInteracao() {
        return dataHoraInteracao;
    }

    public void setDataHoraInteracao(LocalDateTime dataHoraInteracao) {
        this.dataHoraInteracao = dataHoraInteracao;
    }
}