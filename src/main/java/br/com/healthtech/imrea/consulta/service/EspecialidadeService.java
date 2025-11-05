package br.com.healthtech.imrea.consulta.service;

import br.com.healthtech.imrea.consulta.domain.Especialidade;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EspecialidadeService {

    public Especialidade buscarOuCriarEspecialidade(String nomeEspecialidade) {
        if (nomeEspecialidade == null || nomeEspecialidade.isEmpty())
            throw new IllegalArgumentException("Nome da especialidade inválido!");

        Especialidade especialidade = Especialidade.find("nomeEspecialidade = ?1", nomeEspecialidade).firstResult();

        if (especialidade == null)
            throw new IllegalArgumentException("Especialidade não encontrada!");
        else
            return especialidade;
    }

}
