package br.com.healthtech.imrea.agendamento.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="TB_CAR_PROFISSIONAL_SAUDE")
public class Profissional extends PanacheEntityBase {

    @Id
    @SequenceGenerator(name = "profissionalSequence", sequenceName = "TB_CAR_PROFISSIONAL_SAUDE_id_profissional_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profissionalSequence")
    @Column(name="id_profissional")
    private Long idProfissional;

    @Column(name="nome_profissional")
    private String nomeProfissional;

    @Column(name="especialidade_profissional")
    private String especialidadeProfissional;

    @Column(name="dt_criacao")
    private LocalDateTime dtCriacaoProfissional;

    public Profissional() {
    }

    public Profissional(String nomeProfissional, String especialidadeProfissional) {
        this.nomeProfissional = nomeProfissional;
        this.especialidadeProfissional = especialidadeProfissional;
    }

    public Long getIdProfissional() {
        return idProfissional;
    }

    public void setIdProfissional(Long idProfissional) {
        this.idProfissional = idProfissional;
    }

    public String getNomeProfissional() {
        return nomeProfissional;
    }

    public void setNomeProfissional(String nomeProfissional) {
        this.nomeProfissional = nomeProfissional;
    }

    public String getEspecialidadeProfissional() {
        return especialidadeProfissional;
    }

    public void setEspecialidadeProfissional(String especialidadeProfissional) {
        this.especialidadeProfissional = especialidadeProfissional;
    }

    public LocalDateTime getDtCriacaoProfissional() {
        return dtCriacaoProfissional;
    }

    public void setDtCriacaoProfissional(LocalDateTime dtCriacaoProfissional) {
        this.dtCriacaoProfissional = dtCriacaoProfissional;
    }
}