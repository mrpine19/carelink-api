package br.com.healthtech.imrea.agendamento.domain;

import br.com.healthtech.imrea.usuario.domain.Usuario;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_CAR_UPLOAD_LOG")
public class UploadLog extends PanacheEntityBase {

    @Id
    @SequenceGenerator(name = "uploadSequence", sequenceName = "TB_CAR_UPLOAD_LOG_id_upload_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "uploadSequence")
    @Column(name="id_upload")
    private Long idUpload;

    @ManyToOne()
    @JoinColumn(name="id_usuario")
    private Usuario usuario;

    @Column(name="data_hora_upload")
    private LocalDateTime dataHoraUpload;

    @Column(name="nome_arquivo")
    private String nomeArquivo;

    @Column(name="status_upload")
    private String statusUpload;

    @Column (name="num_registros_processados")
    private int numRegistrosProcessados;

    @Column(name="num_registros_com_erro")
    private int numRegistrosComErro;

    @Column(name="detalhes_erros")
    private String detalhesErros;

    public UploadLog() {
    }

    public Long getIdUpload() {
        return idUpload;
    }

    public void setIdUpload(Long idUpload) {
        this.idUpload = idUpload;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getDataHoraUpload() {
        return dataHoraUpload;
    }

    public void setDataHoraUpload(LocalDateTime dataHoraUpload) {
        this.dataHoraUpload = dataHoraUpload;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public String getStatusUpload() {
        return statusUpload;
    }

    public void setStatusUpload(String statusUpload) {
        this.statusUpload = statusUpload;
    }

    public int getNumRegistrosProcessados() {
        return numRegistrosProcessados;
    }

    public void setNumRegistrosProcessados(int numRegistrosProcessados) {
        this.numRegistrosProcessados = numRegistrosProcessados;
    }

    public int getNumRegistrosComErro() {
        return numRegistrosComErro;
    }

    public void setNumRegistrosComErro(int numRegistrosComErro) {
        this.numRegistrosComErro = numRegistrosComErro;
    }

    public String getDetalhesErros() {
        return detalhesErros;
    }

    public void setDetalhesErros(String detalhesErros) {
        this.detalhesErros = detalhesErros;
    }
}