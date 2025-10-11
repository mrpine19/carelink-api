package br.com.healthtech.imrea.paciente.dto;

public class LinhaDoTempoDTO {
    private long idLinhaDoTempo;
    private String tipo;
    private String data;
    private String titulo;

    public long getIdLinhaDoTempo() {
        return idLinhaDoTempo;
    }

    public void setIdLinhaDoTempo(long idLinhaDoTempo) {
        this.idLinhaDoTempo = idLinhaDoTempo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
