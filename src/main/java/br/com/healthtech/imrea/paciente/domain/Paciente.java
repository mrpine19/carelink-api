package br.com.healthtech.imrea.paciente.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="TB_CAR_PACIENTE")
public class Paciente extends PanacheEntityBase {

    @Id
    @SequenceGenerator(name = "pacienteSequence", sequenceName = "TB_CAR_PACIENTE_id_paciente_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pacienteSequence")
    @Column(name="id_paciente")
    public Long idPaciente;

    @ManyToMany
    @JoinTable(name = "TB_CAR_PACIENTE_CUIDADOR",
            joinColumns = @JoinColumn(name = "id_paciente"),
            inverseJoinColumns = @JoinColumn(name = "id_cuidador"))
    public Set<Cuidador> cuidadores = new HashSet<>();

    @Column(name="nome_paciente")
    public String nomePaciente;

    @Column(name="celular_paciente")
    public String telefonePaciente;

    @Column(name="data_nascimento_paciente")
    public Date dataNascimentoPaciente;

    @Column(name="dt_criacao")
    public LocalDateTime dtCriacaoPaciente;

    public Paciente() {
    }

    public Paciente(String nomePaciente, String telefonePaciente, Date dataNascimentoPaciente) {
        this.nomePaciente = nomePaciente;
        this.telefonePaciente = telefonePaciente;
        this.dataNascimentoPaciente = dataNascimentoPaciente;
    }
}
