package br.com.healthtech.imrea.paciente.dto;

import java.util.List;

public class PacienteDTO {

    private Long idPaciente;
    private String nomePaciente;
    private String telefonePaciente;
    private CuidadorDTO cuidador;
    private String scoreDeRisco;
    private String nivelDeRisco;
    private List<String> fatoresDeRisco;
    private ConsultaDTO proximaConsulta;
    private List<LinhaDoTempoDTO> linhaDoTempo;

    public Long getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Long idPaciente) {
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

    public CuidadorDTO getCuidador() {
        return cuidador;
    }

    public void setCuidador(CuidadorDTO cuidador) {
        this.cuidador = cuidador;
    }
}