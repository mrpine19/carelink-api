package br.com.healthtech.imrea.alerta.dto;

import java.util.List;

public class AlertaDTO {

    private String idPaciente;
    private String nomePaciente;
    private String telefonePaciente;
    private String idConsulta;
    private String nomeMedico;
    private String especialidadeConsulta;
    private String horaConsulta;
    private float scoreDeRisco;
    private NivelRisco nivelDeRisco;
    private List<String> fatoresDeRisco;
    private StatusAlerta statusAlerta;
    private PrioridadeAlerta prioridadeAlerta;

    public enum NivelRisco {
        ALTO,
        MEDIO,
        BAIXO
    }

    public enum StatusAlerta {
        NOVO,
        EM_TRATAMENTO,
        RESOLVIDO
    }

    public enum PrioridadeAlerta {
        ALTA,
        MEDIA,
        BAIXA
    }

    public String getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(String idPaciente) {
        this.idPaciente = idPaciente;
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

    public String getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(String idConsulta) {
        this.idConsulta = idConsulta;
    }

    public String getNomeMedico() {
        return nomeMedico;
    }

    public void setNomeMedico(String nomeMedico) {
        this.nomeMedico = nomeMedico;
    }

    public String getEspecialidadeConsulta() {
        return especialidadeConsulta;
    }

    public void setEspecialidadeConsulta(String especialidadeConsulta) {
        this.especialidadeConsulta = especialidadeConsulta;
    }

    public String getHoraConsulta() {
        return horaConsulta;
    }

    public void setHoraConsulta(String horaConsulta) {
        this.horaConsulta = horaConsulta;
    }

    public float getScoreDeRisco() {
        return scoreDeRisco;
    }

    public void setScoreDeRisco(float scoreDeRisco) {
        this.scoreDeRisco = scoreDeRisco;
    }

    public NivelRisco getNivelDeRisco() {
        return nivelDeRisco;
    }

    public void setNivelDeRisco(NivelRisco nivelDeRisco) {
        this.nivelDeRisco = nivelDeRisco;
    }

    public List<String> getFatoresDeRisco() {
        return fatoresDeRisco;
    }

    public void setFatoresDeRisco(List<String> fatoresDeRisco) {
        this.fatoresDeRisco = fatoresDeRisco;
    }
    public StatusAlerta getStatusAlerta() {
        return statusAlerta;
    }

    public void setStatusAlerta(StatusAlerta statusAlerta) {
        this.statusAlerta = statusAlerta;
    }

    public PrioridadeAlerta getPrioridadeAlerta() {
        return prioridadeAlerta;
    }

    public void setPrioridadeAlerta(PrioridadeAlerta prioridadeAlerta) {
        this.prioridadeAlerta = prioridadeAlerta;
    }
}