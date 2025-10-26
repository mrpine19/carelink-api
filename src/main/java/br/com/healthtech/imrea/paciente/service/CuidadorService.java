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
        if (cuidador.getNomeCuidador() == null || cuidador.getNomeCuidador().isEmpty())
            return null;

        Cuidador cuidadorExistente = Cuidador.find("nomeCuidador = ?1 and telefoneCuidador = ?2", cuidador.getNomeCuidador(), cuidador.getTelefoneCuidador()).firstResult();
        if (cuidadorExistente == null){
            cuidador.setDtCriacaoCuidador(LocalDateTime.now());
            cuidador.persist();
            logger.info("Cuidador {} salvo com sucesso!", cuidador.getNomeCuidador());
            return cuidador;
        }else{
            logger.info("Cuidador {} já existe!", cuidador.getNomeCuidador());
            return cuidadorExistente;
        }
    }
}
