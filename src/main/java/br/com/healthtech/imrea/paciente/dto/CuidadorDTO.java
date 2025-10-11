package br.com.healthtech.imrea.paciente.dto;

public class CuidadorDTO {

    private long idCuidador;
    private String nomeCuidador;
    private String telefoneCuidador;

    public long getIdCuidador() {
        return idCuidador;
    }

    public void setIdCuidador(long idCuidador) {
        this.idCuidador = idCuidador;
    }

    public String getNomeCuidador() {
        return nomeCuidador;
    }

    public void setNomeCuidador(String nomeCuidador) {
        this.nomeCuidador = nomeCuidador;
    }

    public String getTelefoneCuidador() {
        return telefoneCuidador;
    }

    public void setTelefoneCuidador(String telefoneCuidador) {
        this.telefoneCuidador = telefoneCuidador;
    }
}
