package br.com.healthtech.imrea.agendamento.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
@Entity
@Table(name="TB_CAR_PROFISSIONAL_SAUDE")
public class Profissional extends PanacheEntity {

    @Column(name="nome_profissional")
    public String nomeProfissional;

    @Column(name="especialidade_profissional")
    public String especialidadeProfissional;

    @Column(name="dt_criacao_profissional")
    public LocalDateTime dtCriacaoProfissional;

    public Profissional() {
    }

    public Profissional(String nomeProfissional, String especialidadeProfissional) {
        this.nomeProfissional = nomeProfissional;
        this.especialidadeProfissional = especialidadeProfissional;
    }
}
