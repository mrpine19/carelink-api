package br.com.healthtech.imrea.paciente.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="TB_CAR_PACIENTE")
public class Paciente extends PanacheEntity {

    @Column(name="nome_paciente_mascarado")
    public String nomePaciente;

    @Column(name="telefoe_paciente_mascarado")
    public String telefonePaciente;

    @Column(name="idade_paciente_marcarado")
    public int idadePaciente;

    @Column(name="dt_criacao_paciente")
    public LocalDateTime dtCriacaoPaciente;

    public Paciente() {
    }

    public Paciente(String nomePaciente, String telefonePaciente) {
        this.nomePaciente = nomePaciente;
        this.telefonePaciente = telefonePaciente;
    }
}
