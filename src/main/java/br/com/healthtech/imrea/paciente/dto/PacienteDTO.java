package br.com.healthtech.imrea.paciente.dto;

import br.com.healthtech.imrea.interacao.dto.LinhaDoTempoDTO;

import java.util.List;

public class PacienteDTO {

    private Long idPaciente;
    private String nomePaciente;
    private String telefonePaciente;
    private String bairroPaciente;
    private String dataNascimentoPaciente;
    private CuidadorDTO cuidador;
    private float scoreDeRisco;
    private String nivelDeRisco;
    private List<String> fatoresDeRisco;
    private ConsultaPacienteDTO proximaConsulta;
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

    public String getBairroPaciente() {
        return bairroPaciente;
    }

    public void setBairroPaciente(String bairroPaciente) {
        this.bairroPaciente = bairroPaciente;
    }

    public String getDataNascimentoPaciente() {
        return dataNascimentoPaciente;
    }

    public void setDataNascimentoPaciente(String dataNascimentoPaciente) {
        this.dataNascimentoPaciente = dataNascimentoPaciente;
    }

    public CuidadorDTO getCuidador() {
        return cuidador;
    }

    public void setCuidador(CuidadorDTO cuidador) {
        this.cuidador = cuidador;
    }

    public float getScoreDeRisco() {
        return scoreDeRisco;
    }

    public void setScoreDeRisco(float scoreDeRisco) {
        this.scoreDeRisco = scoreDeRisco;
    }

    public String getNivelDeRisco() {
        return nivelDeRisco;
    }

    public void setNivelDeRisco(String nivelDeRisco) {
        this.nivelDeRisco = nivelDeRisco;
    }

    public List<String> getFatoresDeRisco() {
        return fatoresDeRisco;
    }

    public void setFatoresDeRisco(List<String> fatoresDeRisco) {
        this.fatoresDeRisco = fatoresDeRisco;
    }

    public ConsultaPacienteDTO getProximaConsulta() {
        return proximaConsulta;
    }

    public void setProximaConsulta(ConsultaPacienteDTO proximaConsulta) {
        this.proximaConsulta = proximaConsulta;
    }

    public List<LinhaDoTempoDTO> getLinhaDoTempo() {
        return linhaDoTempo;
    }

    public void setLinhaDoTempo(List<LinhaDoTempoDTO> linhaDoTempo) {
        this.linhaDoTempo = linhaDoTempo;
    }
}