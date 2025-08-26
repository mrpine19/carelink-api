package br.com.healthtech.imrea.domain;

import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "TB_CAR_UPLOAD_LOG")
public class UploadLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idLog;
    private Long idUsuarioUpload;
    private Date dataHoraUpload;
    private String nomeArquivo;
    private String statusUpload;
    private int numRegistrosProcessados;
    private int numRegistrosComErro;
    private String detalhesErros;

    public UploadLog(Long idUsuarioUpload, String nomeArquivo, Date dataHoraUpload) {
        this.idUsuarioUpload = idUsuarioUpload;
        this.nomeArquivo = nomeArquivo;
        this.dataHoraUpload = dataHoraUpload;
    }

    public UploadLog() {

    }
}
