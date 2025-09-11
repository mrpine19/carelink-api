package br.com.healthtech.imrea.agendamento.domain;

import br.com.healthtech.imrea.usuario.domain.Usuario;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "TB_CAR_UPLOAD_LOG")
public class UploadLog extends PanacheEntityBase {

    @Id
    @SequenceGenerator(name = "uploadSequence", sequenceName = "TB_CAR_UPLOAD_LOG_id_upload_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "uploadSequence")
    @Column(name="id_upload")
    public Long idUpload;

    @ManyToOne()
    @JoinColumn(name="id_usuario")
    public Usuario usuario;

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
