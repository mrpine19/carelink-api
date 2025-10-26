package br.com.healthtech.imrea.usuario.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name="TB_CAR_USUARIO")
public class Usuario extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="id_usuario")
    private Long idUsuario;

    @Column(name="nome_usuario")
    private String nomeUsuario;

    @Column(name="papel_usuario")
    private String papelUsuario;

    public Usuario() {
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getPapelUsuario() {
        return papelUsuario;
    }

    public void setPapelUsuario(String papelUsuario) {
        this.papelUsuario = papelUsuario;
    }
}