package br.com.healthtech.imrea.agendamento.dto;

public class UploadDTO {
    public String status;
    public int sucesso;
    public int erros;

    // Construtor para facilitar a criação do objeto
    public UploadDTO(String status, int sucesso, int erros) {
        this.status = status;
        this.sucesso = sucesso;
        this.erros = erros;
    }
}
