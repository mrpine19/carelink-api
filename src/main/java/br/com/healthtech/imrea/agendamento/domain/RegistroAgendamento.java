package br.com.healthtech.imrea.agendamento.domain;

import com.alibaba.excel.annotation.ExcelProperty;

public class RegistroAgendamento {

    @ExcelProperty("Nome medico")
    private String nomeMedico;

    @ExcelProperty("Data agenda")
    private String dataAgendamento;

    @ExcelProperty("Nome paciente")
    private String nomePaciente;

    @ExcelProperty("Número celular")
    private String numeroPaciente;

    @ExcelProperty("Data nascimento")
    private String dataNascimentoPaciente;

    @ExcelProperty("Nome acompanhante")
    private String nomeAcompanhante;

    @ExcelProperty("Número acompanhante")
    private String numeroAcompanhante;

    @ExcelProperty("Especialidade")
    private String especialidade;

    @ExcelProperty("Hora Agenda")
    private String horaAgendamento;

    @ExcelProperty("Link")
    private String linkConsulta;

    @ExcelProperty("Código")
    private String codigoConsulta;

    @ExcelProperty("OBS")
    private String obsAgendamento;

    @ExcelProperty("CEP")
    private String cep;

    public void normalizarCamposNulos(){
        this.nomeMedico = this.nomeMedico == null ? "" : this.nomeMedico;
        this.dataAgendamento = this.dataAgendamento == null ? "" : this.dataAgendamento;
        this.nomePaciente = this.nomePaciente == null ? "" : this.nomePaciente;
        this.numeroPaciente = this.numeroPaciente == null ? "" : this.numeroPaciente;
        this.dataNascimentoPaciente = this.dataNascimentoPaciente == null ? "" : this.dataNascimentoPaciente;
        this.nomeAcompanhante = this.nomeAcompanhante == null ? "" : this.nomeAcompanhante;
        this.numeroAcompanhante = this.numeroAcompanhante == null ? "" : this.numeroAcompanhante;
        this.especialidade = this.especialidade == null ? "" : this.especialidade;
        this.horaAgendamento = this.horaAgendamento == null ? "" : this.horaAgendamento;
        this.linkConsulta = this.linkConsulta == null ? "" : this.linkConsulta;
        this.codigoConsulta = this.codigoConsulta == null ? "" : this.codigoConsulta;
        this.obsAgendamento = this.obsAgendamento == null ? "" : this.obsAgendamento;
        this.cep = this.cep == null ? "" : this.cep;
    }

    public String getNomeMedico() {
        return nomeMedico;
    }

    public void setNomeMedico(String nomeMedico) {
        this.nomeMedico = nomeMedico;
    }

    public String getDataAgendamento() {
        return dataAgendamento;
    }

    public void setDataAgendamento(String dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public void setNomePaciente(String nomePaciente) {
        this.nomePaciente = nomePaciente;
    }

    public String getNumeroPaciente() {
        return numeroPaciente;
    }

    public void setNumeroPaciente(String numeroPaciente) {
        this.numeroPaciente = numeroPaciente;
    }

    public String getDataNascimentoPaciente() {
        return dataNascimentoPaciente;
    }

    public void setDataNascimentoPaciente(String dataNascimentoPaciente) {
        this.dataNascimentoPaciente = dataNascimentoPaciente;
    }

    public String getNomeAcompanhante() {
        return nomeAcompanhante;
    }

    public void setNomeAcompanhante(String nomeAcompanhante) {
        this.nomeAcompanhante = nomeAcompanhante;
    }

    public String getNumeroAcompanhante() {
        return numeroAcompanhante;
    }

    public void setNumeroAcompanhante(String numeroAcompanhante) {
        this.numeroAcompanhante = numeroAcompanhante;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getHoraAgendamento() {
        return horaAgendamento;
    }

    public void setHoraAgendamento(String horaAgendamento) {
        this.horaAgendamento = horaAgendamento;
    }

    public String getLinkConsulta() {
        return linkConsulta;
    }

    public void setLinkConsulta(String linkConsulta) {
        this.linkConsulta = linkConsulta;
    }

    public String getCodigoConsulta() {
        return codigoConsulta;
    }

    public void setCodigoConsulta(String codigoConsulta) {
        this.codigoConsulta = codigoConsulta;
    }

    public String getObsAgendamento() {
        return obsAgendamento;
    }

    public void setObsAgendamento(String obsAgendamento) {
        this.obsAgendamento = obsAgendamento;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }
}