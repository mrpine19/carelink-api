package br.com.healthtech.imrea.paciente.domain;

import br.com.healthtech.imrea.agendamento.domain.Consulta;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name="TB_CAR_INTERACAO_AUTOMATIZADA")
public class InteracaoAutomatizada extends PanacheEntityBase {

    @Id
    @SequenceGenerator(name = "interacaoSequence", sequenceName = "TB_CAR_INTERACAO_AUTOMATIZADA_id_interacao_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "interacaoSequence")
    @Column(name="id_interacao")
    public Long idInteracao;

    @OneToOne
    @JoinColumn(name="id_consulta")
    public Consulta consulta;

    @Column(name="tipo_interacao")
    public String tipoInteracao;

    @Column(name="status_interacao")
    public String statusInteracao;

    @Column(name="data_hora_interacao")
    public Date dataHoraInteracao;

    @Column(name="dt_criacao")
    public LocalDateTime dataCriacao;

    public InteracaoAutomatizada() {
    }

    public InteracaoAutomatizada(Consulta consulta) {
        this.consulta = consulta;
    }
}
