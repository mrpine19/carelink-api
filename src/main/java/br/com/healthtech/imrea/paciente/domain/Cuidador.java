package br.com.healthtech.imrea.paciente.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="TB_CAR_CUIDADOR")
public class Cuidador extends PanacheEntityBase {

    @Id
    @SequenceGenerator(name = "cuidadorSequence", sequenceName = "TB_CAR_CUIDADOR_id_cuidador_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cuidadorSequence")
    @Column(name="id_cuidador")
    public Long idCuidador;

    @ManyToMany(mappedBy = "cuidadores")
    public Set<Paciente> pacientes = new HashSet<>();

    @Column(name="nome_cuidador")
    public String nomeCuidador;

    @Column(name="telefone_cuidador")
    public String telefoneCuidador;

    @Column(name="dt_criacao")
    public LocalDateTime dtCriacaoCuidador;

    public Cuidador() {
    }

    public Cuidador(String nomeCuidador, String telefoneCuidador) {
        this.nomeCuidador = nomeCuidador;
        this.telefoneCuidador = telefoneCuidador;
    }
}
