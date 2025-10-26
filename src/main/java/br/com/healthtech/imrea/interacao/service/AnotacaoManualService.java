package br.com.healthtech.imrea.interacao.service;

import br.com.healthtech.imrea.interacao.domain.AnotacaoManual;
import br.com.healthtech.imrea.interacao.dto.AnotacaoInputDTO;
import br.com.healthtech.imrea.interacao.dto.AnotacaoUpdateDTO;
import br.com.healthtech.imrea.interacao.dto.InteracaoEquipeDTO;
import br.com.healthtech.imrea.paciente.domain.Paciente;
import br.com.healthtech.imrea.usuario.domain.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class AnotacaoManualService {

    private static final Logger logger = LoggerFactory.getLogger(AnotacaoManualService.class);

    public List<InteracaoEquipeDTO> buscarHistoricoEquipePorPaciente(Long idPaciente) {
        List<AnotacaoManual> anotacoes = AnotacaoManual.find("paciente.idPaciente = ?1 order by dataHoraAnotacao desc", idPaciente).list();
        List<InteracaoEquipeDTO> historico = new ArrayList<>();

        for (AnotacaoManual anotacao : anotacoes) {
            InteracaoEquipeDTO interacao = new InteracaoEquipeDTO();
            interacao.setId(anotacao.getIdAnotacao().toString());
            interacao.setTipo("ANOTACAO_EQUIPE");
            interacao.setData(anotacao.getDataHoraAnotacao().toLocalDate().toString());
            interacao.setHora(String.format("%02d:%02d", anotacao.getDataHoraAnotacao().getHour(), anotacao.getDataHoraAnotacao().getMinute()));
            interacao.setAnotacao(anotacao.getConteudoAnotacao());
            interacao.setIdUsuario(anotacao.getUsuario().getIdUsuario().toString());
            interacao.setNomeUsuario(anotacao.getUsuario().getNomeUsuario());
            historico.add(interacao);
        }
        return historico;
    }

    @Transactional
    public void salvarAnotacao(AnotacaoInputDTO anotacaoInputDTO) {
        if (anotacaoInputDTO.getIdPaciente() == null || anotacaoInputDTO.getIdPaciente() <= 0){
            logger.warn("Id do paciente inválido: {}", anotacaoInputDTO.getIdPaciente());
            throw new BadRequestException("Id do paciente inválido");
        }

        Paciente paciente = Paciente.findById(anotacaoInputDTO.getIdPaciente());
        if (paciente == null){
            logger.warn("Paciente não encontrado: {}", anotacaoInputDTO.getIdPaciente());
            throw new NotFoundException("Paciente não encontrado");
        }

        if (anotacaoInputDTO.getIdUsuario() == null || anotacaoInputDTO.getIdUsuario() <= 0){
            logger.warn("Id do usuário inválido: {}", anotacaoInputDTO.getIdUsuario());
            throw new BadRequestException ("Id do usuário inválido");
        }

        Usuario usuario = Usuario.findById(anotacaoInputDTO.getIdUsuario());

        if (usuario == null){
            logger.warn("Usuário não encontrado: {}", anotacaoInputDTO.getIdUsuario());
            throw new NotFoundException("Usuário não encontrado");
        }

        if (anotacaoInputDTO.getConteudoAnotacao() == null || anotacaoInputDTO.getConteudoAnotacao().isEmpty()){
            logger.warn("Conteúdo da anotação inválido");
            throw new BadRequestException("Conteúdo da anotação inválido");
        }

        AnotacaoManual anotacaoManual = new AnotacaoManual(paciente, usuario, anotacaoInputDTO.getConteudoAnotacao());
        anotacaoManual.persist();
        logger.info("Anotação manual salva com sucesso para o paciente {} pelo usuário {}", paciente.getNomePaciente(), usuario.getNomeUsuario());
    }

    public void alterarAnotacao(AnotacaoUpdateDTO anotacaoUpdateDTO) {
        if (anotacaoUpdateDTO.getIdAnotacao() == null || anotacaoUpdateDTO.getIdAnotacao() <= 0){
            logger.warn("Id da anotação inválido: {}", anotacaoUpdateDTO.getIdAnotacao());
            throw new BadRequestException("Id da anotação inválido");
        }

        AnotacaoManual anotacaoManual = AnotacaoManual.findById(anotacaoUpdateDTO.getIdAnotacao());
        if (anotacaoManual == null){
            logger.warn("Anotação não encontrada: {}", anotacaoUpdateDTO.getIdAnotacao());
            throw new NotFoundException("Anotação não encontrada");
        }

        if (anotacaoUpdateDTO.getIdUsuario() == null || anotacaoUpdateDTO.getIdUsuario() <= 0){
            logger.warn("Id do usuário inválido: {}", anotacaoUpdateDTO.getIdUsuario());
            throw new BadRequestException ("Id do usuário inválido");
        }

        Usuario usuario = Usuario.findById(anotacaoUpdateDTO.getIdUsuario());

        if (usuario == null){
            logger.warn("Usuário não encontrado: {}", anotacaoUpdateDTO.getIdUsuario());
            throw new NotFoundException("Usuário não encontrado");
        }

        if (anotacaoUpdateDTO.getNovoConteudo() == null || anotacaoUpdateDTO.getNovoConteudo().isEmpty()){
            logger.warn("Conteúdo da anotação inválido");
            throw new BadRequestException("Conteúdo da anotação inválido");
        }

        anotacaoManual.setConteudoAnotacao(anotacaoUpdateDTO.getNovoConteudo());
        anotacaoManual.persist();
        logger.info("Anotação manual com id {} alterada com sucesso pelo usuário {}", anotacaoManual.getIdAnotacao(), usuario.getNomeUsuario());
    }
}
