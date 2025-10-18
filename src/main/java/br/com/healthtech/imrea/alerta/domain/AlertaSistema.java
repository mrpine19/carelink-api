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
    public Long idAlerta;

    @ManyToOne
    @JoinColumn(name="id_consulta")
    public Consulta consulta;

    @ManyToOne
    @JoinColumn(name="id_paciente", nullable = false)
    public Paciente paciente;

    @ManyToOne
    @JoinColumn(name="id_cuidador")
    public Cuidador cuidador;

    @ManyToOne
    @JoinColumn(name="id_interacao")
    public InteracaoAutomatizada interacaoAutomatizada;

    @Column(name="tipo_alerta")
    public String tipoAlerta;

    @Column(name="status_alerta")
    public String statusAlerta;

    @Column(name="prioridade_alerta")
    public String prioridadeAlerta;

    @Column(name="detalhes_contribuicao_risco")
    public String detalhesContribuicaoRisco;

    @Column(name="acao_tomada")
    public String acaoTomada;

    @Column(name="data_hora_Acao")
    public LocalDateTime dataHoraAcao;

    @Column(name="dt_criacao")
    public LocalDateTime dtCriacaoAlerta;
}