package br.com.healthtech.imrea.consulta.domain;

import br.com.healthtech.imrea.paciente.domain.Paciente;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name="TB_CAR_ESPECIALIDADE")
public class Especialidade extends PanacheEntityBase {

    @Id
    @SequenceGenerator(name = "especialidadeSequence", sequenceName = "TB_CAR_ESPECIALIDADE_id_especialidade_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "especialidadeSequence")
    @Column(name="id_especialidade")
    private Long idEspecialidade;

    @Column(name = "nome_especialidade")
    private String nomeEspecialidade;

    public Long getIdEspecialidade() {
        return idEspecialidade;
    }

    public void setIdEspecialidade(Long idEspecialidade) {
        this.idEspecialidade = idEspecialidade;
    }

    public String getNomeEspecialidade() {
        return nomeEspecialidade;
    }

    public void setNomeEspecialidade(String nomeEspecialidade) {
        this.nomeEspecialidade = nomeEspecialidade;
    }
}
