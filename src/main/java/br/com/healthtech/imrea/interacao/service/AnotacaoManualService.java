package br.com.healthtech.imrea.interacao.service;

import br.com.healthtech.imrea.interacao.domain.AnotacaoManual;
import br.com.healthtech.imrea.interacao.dto.InteracaoEquipeDTO;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class AnotacaoManualService {

    public List<InteracaoEquipeDTO> buscarHistoricoEquipePorPaciente(Long idPaciente) {
        List<AnotacaoManual> anotacoes = AnotacaoManual.find("paciente.idPaciente = ?1 order by dataHoraAnotacao desc", idPaciente).list();
        List<InteracaoEquipeDTO> historico = new ArrayList<>();

        for (AnotacaoManual anotacao : anotacoes) {
            InteracaoEquipeDTO interacao = new InteracaoEquipeDTO();
            interacao.setTipo("ANOTACAO_EQUIPE");
            interacao.setData(anotacao.dataHoraAnotacao.toLocalDate().toString());
            interacao.setHora(String.format("%02d:%02d", anotacao.dataHoraAnotacao.getHour(), anotacao.dataHoraAnotacao.getMinute()));
            interacao.setAnotacao(anotacao.conteudoAnotacao);
            interacao.setIdUsuario(anotacao.usuario.idUsuario.toString());
            interacao.setNomeUsuario(anotacao.usuario.nomeUsuario);
            historico.add(interacao);
        }
        return historico;
    }
}
