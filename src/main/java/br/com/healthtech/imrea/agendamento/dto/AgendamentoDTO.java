package br.com.healthtech.imrea.agendamento.dto;

public class AgendamentoDTO {
    private String id;
    private String title;
    private String patientName;
    private String profissionalName;
    private String category;
    private String status;
    private String modalidadeReal;
    private String anotacoes;
    private String color;
    private String start; // Representa ISO String da data (ex: "2025-10-30T13:00:00Z")
    private String end;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getProfissionalName() {
        return profissionalName;
    }

    public void setProfissionalName(String profissionalName) {
        this.profissionalName = profissionalName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getModalidadeReal() {
        return modalidadeReal;
    }

    public void setModalidadeReal(String modalidadeReal) {
        this.modalidadeReal = modalidadeReal;
    }

    public String getAnotacoes() {
        return anotacoes;
    }

    public void setAnotacoes(String anotacoes) {
        this.anotacoes = anotacoes;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
