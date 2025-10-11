package br.com.healthtech.imrea.paciente.service;

import br.com.healthtech.imrea.paciente.domain.Cuidador;
import br.com.healthtech.imrea.paciente.domain.Paciente;
import br.com.healthtech.imrea.paciente.dto.CuidadorDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;

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

    @Transactional
    public CuidadorDTO buscarCuidadoresPorPaciente(Long idPaciente) {
        if (idPaciente == null || idPaciente <= 0) {
            throw new IllegalArgumentException("ID do paciente inválido");
        }
        Paciente paciente = Paciente.findById(idPaciente);
        if (paciente == null)
            throw new IllegalArgumentException("Paciente não encontrado");

        Cuidador cuidador = Cuidador.find("paciente.idPaciente = ?1", idPaciente).firstResult();
        if (cuidador == null)
            return null;
        CuidadorDTO cuidadorDTO = new CuidadorDTO();
        cuidadorDTO.setIdCuidador(cuidador.idCuidador);
        cuidadorDTO.setNomeCuidador(cuidador.nomeCuidador);
        cuidadorDTO.setTelefoneCuidador(cuidador.telefoneCuidador);
        return cuidadorDTO;
    }
}
