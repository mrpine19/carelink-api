package br.com.healthtech.imrea.interacao.domain;

import br.com.healthtech.imrea.agendamento.domain.Consulta;
import br.com.healthtech.imrea.paciente.domain.Paciente;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.jboss.logging.Logger;

import java.util.Date;

@Entity
@Table(name="TB_CAR_INTERACAO_AUTOMATIZADA")
public class InteracaoAutomatizada extends PanacheEntityBase {

    private static final Logger LOG = Logger.getLogger(InteracaoAutomatizada.class);

    @Id
    @SequenceGenerator(name = "interacaoSequence", sequenceName = "TB_CAR_INTERACAO_AUTOMATIZADA_id_interacao_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "interacaoSequence")
    @Column(name="id_interacao")
    public Long idInteracao;

    @ManyToOne
    @JoinColumn(name="id_consulta")
    public Consulta consulta;

    @ManyToOne
    @JoinColumn(name="id_paciente")
    public Paciente paciente;

    @Column(name="tipo_interacao")
    public String tipoInteracao;

    @Column(name="receptor_tipo")
    public String receptorTipo;

    @Column(name="status_interacao")
    public String statusInteracao;

    @Column(name="detalhes_interacao")
    public String detalhesInteracao;

    @Column(name="data_hora_interacao")
    public Date dataHoraInteracao;

    public InteracaoAutomatizada() {
        LOG.info("InteracaoAutomatizada instanciada (construtor vazio)");
    }

    public InteracaoAutomatizada(Consulta consulta, Paciente paciente) {
        this.consulta = consulta;
        this.paciente = paciente;
        LOG.infof("InteracaoAutomatizada criada: consultaId=%s, pacienteId=%s",
            consulta != null ? consulta.id : "null",
            paciente != null ? paciente.id : "null");
    }

    public void logDetalhes() {
        LOG.infof("Detalhes da InteracaoAutomatizada: idInteracao=%s, tipoInteracao=%s, receptorTipo=%s, statusInteracao=%s, dataHoraInteracao=%s",
            idInteracao, tipoInteracao, receptorTipo, statusInteracao, dataHoraInteracao);
    }
}
