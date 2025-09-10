package br.com.healthtech.imrea.paciente.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="TB_CAR_PACIENTE")
public class Paciente extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="id_paciente")
    public Long idPaciente;

    @Column(name="nome_paciente")
    public String nomePaciente;

    @Column(name="celular_paciente")
    public String telefonePaciente;

    @Column(name="idade_paciente")
    public int idadePaciente;

    @Column(name="dt_criacao")
    public LocalDateTime dtCriacaoPaciente;

    public Paciente() {
    }

    public Paciente(String nomePaciente, String telefonePaciente) {
        this.nomePaciente = nomePaciente;
        this.telefonePaciente = telefonePaciente;
    }
}
