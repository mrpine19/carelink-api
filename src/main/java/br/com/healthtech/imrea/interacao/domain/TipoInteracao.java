package br.com.healthtech.imrea.interacao.domain;

public enum TipoInteracao {
    LEMBRETE_24H("LEMBRETE_24H"),
    LEMBRETE_1H("LEMBRETE_1H");

    public final String tipo;
    TipoInteracao(String tipo) {
        this.tipo = tipo;
    }
}
