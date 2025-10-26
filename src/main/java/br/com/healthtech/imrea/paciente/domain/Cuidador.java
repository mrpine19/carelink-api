package br.com.healthtech.imrea.paciente.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="TB_CAR_CUIDADOR")
public class Cuidador extends PanacheEntityBase {

    @Id
    @SequenceGenerator(name = "cuidadorSequence", sequenceName = "TB_CAR_CUIDADOR_id_cuidador_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cuidadorSequence")
    @Column(name="id_cuidador")
    private Long idCuidador;

    @ManyToMany(mappedBy = "cuidadores")
    private Set<Paciente> pacientes = new HashSet<>();

    @Column(name="nome_cuidador")
    private String nomeCuidador;

    @Column(name="telefone_cuidador")
    private String telefoneCuidador;

    @Column(name="dt_criacao")
    private LocalDateTime dtCriacaoCuidador;

    public Cuidador() {
    }

    public Cuidador(String nomeCuidador, String telefoneCuidador) {
        this.nomeCuidador = nomeCuidador;
        this.telefoneCuidador = telefoneCuidador;
    }

    public Long getIdCuidador() {
        return idCuidador;
    }

    public void setIdCuidador(Long idCuidador) {
        this.idCuidador = idCuidador;
    }

    public Set<Paciente> getPacientes() {
        return pacientes;
    }

    public void setPacientes(Set<Paciente> pacientes) {
        this.pacientes = pacientes;
    }

    public String getNomeCuidador() {
        return nomeCuidador;
    }

    public void setNomeCuidador(String nomeCuidador) {
        this.nomeCuidador = nomeCuidador;
    }

    public String getTelefoneCuidador() {
        return telefoneCuidador;
    }

    public void setTelefoneCuidador(String telefoneCuidador) {
        this.telefoneCuidador = telefoneCuidador;
    }

    public LocalDateTime getDtCriacaoCuidador() {
        return dtCriacaoCuidador;
    }

    public void setDtCriacaoCuidador(LocalDateTime dtCriacaoCuidador) {
        this.dtCriacaoCuidador = dtCriacaoCuidador;
    }
}