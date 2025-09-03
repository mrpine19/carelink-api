package br.com.healthtech.imrea.agendamento.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "TB_CAR_UPLOAD_LOG")
public class UploadLog extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID idLog;
    public Long idUsuarioUpload;
    public Date dataHoraUpload;
    public String nomeArquivo;
    public String statusUpload;
    public int numRegistrosProcessados;
    public int numRegistrosComErro;
    public String detalhesErros;

}
