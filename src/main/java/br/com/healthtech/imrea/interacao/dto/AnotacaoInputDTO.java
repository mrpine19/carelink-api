package br.com.healthtech.imrea.interacao.dto;

public class AnotacaoInputDTO {

    private Long idPaciente;
    private Long idUsuario;
    private String conteudoAnotacao;

    public AnotacaoInputDTO(Long idPaciente, Long idUsuario, String conteudoAnotacao) {
        this.idPaciente = idPaciente;
        this.idUsuario = idUsuario;
        this.conteudoAnotacao = conteudoAnotacao;
    }

    public Long getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Long idPaciente) {
        this.idPaciente = idPaciente;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getConteudoAnotacao() {
        return conteudoAnotacao;
    }

    public void setConteudoAnotacao(String conteudoAnotacao) {
        this.conteudoAnotacao = conteudoAnotacao;
    }
}