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
    private Long idAnotacao;

    @ManyToOne
    @JoinColumn(name = "id_paciente")
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(name="conteudo_anotacao")
    private String conteudoAnotacao;

    @Column(name="data_hora_anotacao")
    private LocalDateTime dataHoraAnotacao;

    public AnotacaoManual() {
    }

    public AnotacaoManual(Paciente paciente, Usuario usuario, String conteudoAnotacao) {
        this.paciente = paciente;
        this.usuario = usuario;
        this.conteudoAnotacao = conteudoAnotacao;
        this.dataHoraAnotacao = LocalDateTime.now();
    }

    public Long getIdAnotacao() {
        return idAnotacao;
    }

    public void setIdAnotacao(Long idAnotacao) {
        this.idAnotacao = idAnotacao;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getConteudoAnotacao() {
        return conteudoAnotacao;
    }

    public void setConteudoAnotacao(String conteudoAnotacao) {
        this.conteudoAnotacao = conteudoAnotacao;
    }

    public LocalDateTime getDataHoraAnotacao() {
        return dataHoraAnotacao;
    }

    public void setDataHoraAnotacao(LocalDateTime dataHoraAnotacao) {
        this.dataHoraAnotacao = dataHoraAnotacao;
    }
}