package br.com.healthtech.imrea.interacao.service;

import br.com.healthtech.imrea.interacao.dto.InteracaoSistemaDTO;
import br.com.healthtech.imrea.interacao.domain.InteracaoAutomatizada;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class InteracaoAutomatizadaService {

    public List<InteracaoSistemaDTO> buscarHistoricoSistemaPorPaciente(Long idPaciente) {
        List<InteracaoAutomatizada> interacoes = InteracaoAutomatizada.find("paciente.idPaciente = ?1 order by dataHoraInteracao desc", idPaciente).list();
        List<InteracaoSistemaDTO> interacoesDTO = new ArrayList<>();

        for (InteracaoAutomatizada interacao : interacoes) {
            InteracaoSistemaDTO dto = new InteracaoSistemaDTO();
            dto.setTipo("INTERACAO_SISTEMA");
            dto.setData(interacao.getDataHoraInteracao().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            dto.setHora(String.format("%02d:%02d", interacao.getDataHoraInteracao().getHour(), interacao.getDataHoraInteracao().getMinute()));
            dto.setLog(interacao.getDetalhesInteracao());
            interacoesDTO.add(dto);
        }
        return interacoesDTO;
    }
}
