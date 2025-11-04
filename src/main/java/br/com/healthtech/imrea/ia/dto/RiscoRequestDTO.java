package br.com.healthtech.imrea.ia.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RiscoRequestDTO {

    @JsonProperty("idade_paciente")
    private int idadePaciente;

    @JsonProperty("bairro_paciente")
    private String bairroPaciente;

    @JsonProperty("tem_cuidador")
    private int temCuidador;

    @JsonProperty("afinidade_digital_score")
    private int afinidadeDigitalScore;

    @JsonProperty("especialidade_consulta")
    private String especialidadeConsulta;

    @JsonProperty("faltas_consecutivas_historico")
    private int faltasConsecutivasHistorico;

    @JsonProperty("taxa_absenteismo_historica")
    private double taxaAbsenteismoHistorica;

    @JsonProperty("tempo_desde_primeira_consulta_dias")
    private int tempoDesdePrimeiraConsultaDias;

    @JsonProperty("tempo_desde_ultima_consulta_dias")
    private int tempoDesdeUltimaConsultaDias;

    // Getters e Setters

    public int getIdadePaciente() {
        return idadePaciente;
    }

    public void setIdadePaciente(int idadePaciente) {
        this.idadePaciente = idadePaciente;
    }

    public String getBairroPaciente() {
        return bairroPaciente;
    }

    public void setBairroPaciente(String bairroPaciente) {
        this.bairroPaciente = bairroPaciente;
    }

    public int getTemCuidador() {
        return temCuidador;
    }

    public void setTemCuidador(int temCuidador) {
        this.temCuidador = temCuidador;
    }

    public int getAfinidadeDigitalScore() {
        return afinidadeDigitalScore;
    }

    public void setAfinidadeDigitalScore(int afinidadeDigitalScore) {
        this.afinidadeDigitalScore = afinidadeDigitalScore;
    }

    public String getEspecialidadeConsulta() {
        return especialidadeConsulta;
    }

    public void setEspecialidadeConsulta(String especialidadeConsulta) {
        this.especialidadeConsulta = especialidadeConsulta;
    }

    public int getFaltasConsecutivasHistorico() {
        return faltasConsecutivasHistorico;
    }

    public void setFaltasConsecutivasHistorico(int faltasConsecutivasHistorico) {
        this.faltasConsecutivasHistorico = faltasConsecutivasHistorico;
    }

    public double getTaxaAbsenteismoHistorica() {
        return taxaAbsenteismoHistorica;
    }

    public void setTaxaAbsenteismoHistorica(double taxaAbsenteismoHistorica) {
        this.taxaAbsenteismoHistorica = taxaAbsenteismoHistorica;
    }

    public int getTempoDesdePrimeiraConsultaDias() {
        return tempoDesdePrimeiraConsultaDias;
    }

    public void setTempoDesdePrimeiraConsultaDias(int tempoDesdePrimeiraConsultaDias) {
        this.tempoDesdePrimeiraConsultaDias = tempoDesdePrimeiraConsultaDias;
    }

    public int getTempoDesdeUltimaConsultaDias() {
        return tempoDesdeUltimaConsultaDias;
    }

    public void setTempoDesdeUltimaConsultaDias(int tempoDesdeUltimaConsultaDias) {
        this.tempoDesdeUltimaConsultaDias = tempoDesdeUltimaConsultaDias;
    }
}