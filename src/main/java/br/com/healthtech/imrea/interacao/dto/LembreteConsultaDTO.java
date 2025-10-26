package br.com.healthtech.imrea.interacao.dto;

public class LembreteConsultaDTO {
    private String idConsulta;
    private String nomePaciente;
    private String especialidadeConsulta;
    private String nomeProfissional;
    private String dataConsulta;
    private String horaConsulta;

    public String getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(String idConsulta) {
        this.idConsulta = idConsulta;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public void setNomePaciente(String nomePaciente) {
        this.nomePaciente = nomePaciente;
    }

    public String getEspecialidadeConsulta() {
        return especialidadeConsulta;
    }

    public void setEspecialidadeConsulta(String especialidadeConsulta) {
        this.especialidadeConsulta = especialidadeConsulta;
    }

    public String getNomeProfissional() {
        return nomeProfissional;
    }

    public void setNomeProfissional(String nomeProfissional) {
        this.nomeProfissional = nomeProfissional;
    }

    public String getDataConsulta() {
        return dataConsulta;
    }

    public void setDataConsulta(String dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    public String getHoraConsulta() {
        return horaConsulta;
    }

    public void setHoraConsulta(String horaConsulta) {
        this.horaConsulta = horaConsulta;
    }
}