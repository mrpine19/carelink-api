package br.com.healthtech.imrea.paciente.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="TB_CAR_PACIENTE")
public class Paciente extends PanacheEntityBase {

    @Id
    @SequenceGenerator(name = "pacienteSequence", sequenceName = "TB_CAR_PACIENTE_id_paciente_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pacienteSequence")
    @Column(name="id_paciente")
    private Long idPaciente;

    @ManyToMany
    @JoinTable(name = "TB_CAR_PACIENTE_CUIDADOR",
            joinColumns = @JoinColumn(name = "id_paciente"),
            inverseJoinColumns = @JoinColumn(name = "id_cuidador"))
    private Set<Cuidador> cuidadores = new HashSet<>();

    @Column(name="nome_paciente")
    private String nomePaciente;

    @Column(name="celular_paciente")
    private String telefonePaciente;

    @Column(name="data_nascimento_paciente")
    private LocalDate dataNascimentoPaciente;

    @Column(name="score_risco_absenteismo")
    private float scoreDeRisco;

    @Column(name="bairro_paciente")
    private String bairroPaciente;

    @Column(name="dt_criacao")
    private LocalDateTime dtCriacaoPaciente;

    public Paciente() {
    }

    public Paciente(String nomePaciente, String telefonePaciente, LocalDate dataNascimentoPaciente) {
        this.nomePaciente = nomePaciente;
        this.telefonePaciente = telefonePaciente;
        this.dataNascimentoPaciente = dataNascimentoPaciente;
    }

    public Long getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Long idPaciente) {
        this.idPaciente = idPaciente;
    }

    public Set<Cuidador> getCuidadores() {
        return cuidadores;
    }

    public void setCuidadores(Set<Cuidador> cuidadores) {
        this.cuidadores = cuidadores;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public void setNomePaciente(String nomePaciente) {
        this.nomePaciente = nomePaciente;
    }

    public String getTelefonePaciente() {
        return telefonePaciente;
    }

    public void setTelefonePaciente(String telefonePaciente) {
        this.telefonePaciente = telefonePaciente;
    }

    public LocalDate getDataNascimentoPaciente() {
        return dataNascimentoPaciente;
    }

    public void setDataNascimentoPaciente(LocalDate dataNascimentoPaciente) {
        this.dataNascimentoPaciente = dataNascimentoPaciente;
    }

    public float getScoreDeRisco() {
        return scoreDeRisco;
    }

    public void setScoreDeRisco(float scoreDeRisco) {
        this.scoreDeRisco = scoreDeRisco;
    }

    public String getBairroPaciente() {
        return bairroPaciente;
    }

    public void setBairroPaciente(String bairroPaciente) {
        this.bairroPaciente = bairroPaciente;
    }

    public LocalDateTime getDtCriacaoPaciente() {
        return dtCriacaoPaciente;
    }

    public void setDtCriacaoPaciente(LocalDateTime dtCriacaoPaciente) {
        this.dtCriacaoPaciente = dtCriacaoPaciente;
    }
}