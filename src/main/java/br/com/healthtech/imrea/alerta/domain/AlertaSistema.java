package br.com.healthtech.imrea.alerta.domain;

import br.com.healthtech.imrea.agendamento.domain.Consulta;
import br.com.healthtech.imrea.interacao.domain.InteracaoAutomatizada;
import br.com.healthtech.imrea.paciente.domain.Cuidador;
import br.com.healthtech.imrea.paciente.domain.Paciente;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="TB_CAR_ALERTA_SISTEMA")
public class AlertaSistema extends PanacheEntityBase {

    @Id
    @SequenceGenerator(name = "alertaSistemaSequence", sequenceName = "TB_CAR_ALERTA_SISTEMA_id_alerta_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "alertaSistemaSequence")
    @Column(name="id_alerta")
    private Long idAlerta;

    @ManyToOne
    @JoinColumn(name="id_consulta")
    private Consulta consulta;

    @ManyToOne
    @JoinColumn(name="id_paciente", nullable = false)
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name="id_cuidador")
    private Cuidador cuidador;

    @ManyToOne
    @JoinColumn(name="id_interacao")
    private InteracaoAutomatizada interacaoAutomatizada;

    @Column(name="tipo_alerta")
    private String tipoAlerta;

    @Column(name="status_alerta")
    private String statusAlerta;

    @Column(name="prioridade_alerta")
    private String prioridadeAlerta;

    @Column(name="detalhes_contribuicao_risco")
    private String detalhesContribuicaoRisco;

    @Column(name="acao_tomada")
    private String acaoTomada;

    @Column(name="data_hora_Acao")
    private LocalDateTime dataHoraAcao;

    @Column(name="dt_criacao")
    private LocalDateTime dtCriacaoAlerta;

    public AlertaSistema() {
    }

    public Long getIdAlerta() {
        return idAlerta;
    }

    public void setIdAlerta(Long idAlerta) {
        this.idAlerta = idAlerta;
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

    public Cuidador getCuidador() {
        return cuidador;
    }

    public void setCuidador(Cuidador cuidador) {
        this.cuidador = cuidador;
    }

    public InteracaoAutomatizada getInteracaoAutomatizada() {
        return interacaoAutomatizada;
    }

    public void setInteracaoAutomatizada(InteracaoAutomatizada interacaoAutomatizada) {
        this.interacaoAutomatizada = interacaoAutomatizada;
    }

    public String getTipoAlerta() {
        return tipoAlerta;
    }

    public void setTipoAlerta(String tipoAlerta) {
        this.tipoAlerta = tipoAlerta;
    }

    public String getStatusAlerta() {
        return statusAlerta;
    }

    public void setStatusAlerta(String statusAlerta) {
        this.statusAlerta = statusAlerta;
    }

    public String getPrioridadeAlerta() {
        return prioridadeAlerta;
    }

    public void setPrioridadeAlerta(String prioridadeAlerta) {
        this.prioridadeAlerta = prioridadeAlerta;
    }

    public String getDetalhesContribuicaoRisco() {
        return detalhesContribuicaoRisco;
    }

    public void setDetalhesContribuicaoRisco(String detalhesContribuicaoRisco) {
        this.detalhesContribuicaoRisco = detalhesContribuicaoRisco;
    }

    public String getAcaoTomada() {
        return acaoTomada;
    }

    public void setAcaoTomada(String acaoTomada) {
        this.acaoTomada = acaoTomada;
    }

    public LocalDateTime getDataHoraAcao() {
        return dataHoraAcao;
    }

    public void setDataHoraAcao(LocalDateTime dataHoraAcao) {
        this.dataHoraAcao = dataHoraAcao;
    }

    public LocalDateTime getDtCriacaoAlerta() {
        return dtCriacaoAlerta;
    }

    public void setDtCriacaoAlerta(LocalDateTime dtCriacaoAlerta) {
        this.dtCriacaoAlerta = dtCriacaoAlerta;
    }
}