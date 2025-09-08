package br.com.healthtech.imrea.usuario.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name="TB_CAR_USUARIO")
public class Usuario extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="id_usuario")
    public Long idUsuario;

    @Column(name="nome_usuario")
    public String nomeUsuario;

    @Column(name="papel_usuario")
    public String papelUsuario;

}
