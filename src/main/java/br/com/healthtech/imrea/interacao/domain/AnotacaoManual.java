package br.com.healthtech.imrea.interacao.domain;


import br.com.healthtech.imrea.paciente.domain.Paciente;
import br.com.healthtech.imrea.usuario.domain.Usuario;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="TB_CAR_ANOTACAO_MANUAL")
public class AnotacaoManual extends PanacheEntityBase {

    @Id
    @SequenceGenerator(name = "anotacaoManualSequence", sequenceName = "TB_CAR_ANOTACAO_MANUAL_id_anotacao_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "anotacaoManualSequence")
    @Column(name="id_anotacao")
    public Long idAnotacao;

    @ManyToOne
    @JoinColumn(name = "id_paciente")
    public Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    public Usuario usuario;

    @Column(name="conteudo_anotacao")
    public String conteudoAnotacao;

    @Column(name="data_hora_anotacao")
    public LocalDateTime dataHoraAnotacao;

    public AnotacaoManual() {
    }

    public AnotacaoManual(Paciente paciente, Usuario usuario, String conteudoAnotacao) {
        this.paciente = paciente;
        this.usuario = usuario;
        this.conteudoAnotacao = conteudoAnotacao;
        this.dataHoraAnotacao = LocalDateTime.now();
    }
}