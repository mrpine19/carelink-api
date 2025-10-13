package br.com.healthtech.imrea.interacao.dto;

public class AnotacaoUpdateDTO {
    private Long idAnotacao;
    private Long idUsuario;
    private String novoConteudo;

    public AnotacaoUpdateDTO(Long idAnotacao, Long idUsuario, String novoConteudo) {
        this.idAnotacao = idAnotacao;
        this.idUsuario = idUsuario;
        this.novoConteudo = novoConteudo;
    }

    public Long getIdAnotacao() {
        return idAnotacao;
    }

    public void setIdAnotacao(Long idAnotacao) {
        this.idAnotacao = idAnotacao;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNovoConteudo() {
        return novoConteudo;
    }

    public void setNovoConteudo(String novoConteudo) {
        this.novoConteudo = novoConteudo;
    }
}
