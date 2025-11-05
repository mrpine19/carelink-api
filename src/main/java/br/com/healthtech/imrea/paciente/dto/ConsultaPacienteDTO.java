package br.com.healthtech.imrea.paciente.dto;

public class ConsultaPacienteDTO {
    private String dataConsulta;
    private String horaConsulta;
    private String nomeMedico;
    private String especialidadeConsulta;

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
}
