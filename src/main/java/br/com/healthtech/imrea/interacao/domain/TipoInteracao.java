package br.com.healthtech.imrea.interacao.domain;

public enum TipoInteracao {
    LEMBRETE_24H("LEMBRETE_24H"),
    LEMBRETE_1H("LEMBRETE_1H"),
    CONFIRMAR_CONSULTA("CONFIRMAR_CONSULTA"),
    REAGENDAR_CONSULTA("REAGENDAR_CONSULTA");

    private final String tipo;

    TipoInteracao(String tipo) {
        this.tipo = tipo;
    }

    // Getter para acesso
    public String getTipo() {
        return tipo;
    }
}