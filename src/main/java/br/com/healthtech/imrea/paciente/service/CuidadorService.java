package br.com.healthtech.imrea.paciente.service;

import br.com.healthtech.imrea.paciente.domain.Cuidador;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@ApplicationScoped
public class CuidadorService {
    private static final Logger logger = LoggerFactory.getLogger(CuidadorService.class);

    @Transactional
    public Cuidador buscarOuCriarCuidador(Cuidador cuidador){
        if (cuidador.nomeCuidador == null || cuidador.nomeCuidador.isEmpty())
            return null;

        Cuidador cuidadorExistente = Cuidador.find("nomeCuidador = ?1 and telefoneCuidador = ?2", cuidador.nomeCuidador, cuidador.telefoneCuidador).firstResult();
        if (cuidadorExistente == null){
            cuidador.dtCriacaoCuidador = LocalDateTime.now();
            cuidador.persist();
            logger.info("Cuidador {} salvo com sucesso!", cuidador.nomeCuidador);
            return cuidador;
        }else{
            logger.info("Cuidador {} já existe!", cuidador.nomeCuidador);
            return cuidadorExistente;
        }
    }
}
