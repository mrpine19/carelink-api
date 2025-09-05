package br.com.healthtech.imrea.agendamento.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "TB_CAR_UPLOAD_LOG")
public class UploadLog extends PanacheEntity {

    @Column(name="data_hora_upload")
    public Date dataHoraUpload;

    @Column(name="nome_arquivo")
    public String nomeArquivo;

    @Column(name="status_upload")
    public String statusUpload;

    @Column (name="num_registros_processados")
    public int numRegistrosProcessados;

    @Column(name="num_registros_com_erro")
    public int numRegistrosComErro;

    @Column(name="detalhes_erros")
    public String detalhesErros;

}
