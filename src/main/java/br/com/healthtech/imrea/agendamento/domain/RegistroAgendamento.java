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

    @ExcelProperty("Especialidade")
    private String especialidade;

    @ExcelProperty("Nome Profissional Executor")
    private String profissionalExecutor;

    @ExcelProperty("Hora Agenda")
    private String horaAgendamento;

    @ExcelProperty("Link")
    private String linkConsulta;

    @ExcelProperty("Código")
    private int codigoConsulta;

    @ExcelProperty("OBS")
    private String observacao;

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

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getProfissionalExecutor() {
        return profissionalExecutor;
    }

    public void setProfissionalExecutor(String profissionalExecutor) {
        this.profissionalExecutor = profissionalExecutor;
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

    public int getCodigoConsulta() {
        return codigoConsulta;
    }

    public void setCodigoConsulta(int codigoConsulta) {
        this.codigoConsulta = codigoConsulta;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
