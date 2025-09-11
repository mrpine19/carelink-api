package br.com.healthtech.imrea.paciente.service;

import br.com.healthtech.imrea.agendamento.domain.Consulta;
import br.com.healthtech.imrea.agendamento.service.ConsultaService;
import br.com.healthtech.imrea.alerta.service.ChatbotService;
import br.com.healthtech.imrea.paciente.domain.InteracaoAutomatizada;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class InteracaoAutomatizadaService {

    private static final Logger logger = LoggerFactory.getLogger(InteracaoAutomatizadaService.class);

    @Inject
    ConsultaService consultaService;

    @Inject
    ChatbotService chatbotService;

    @Transactional
    public InteracaoAutomatizada buscarOuCriarInteracao(Consulta consulta){
        if(consulta == null)
            throw new IllegalArgumentException("Não foi possível encontrar a consulta");

        InteracaoAutomatizada interacaoExistente = InteracaoAutomatizada.find("consulta = ?1", consulta).firstResult();

        if(interacaoExistente == null){
            InteracaoAutomatizada interacao = new InteracaoAutomatizada(consulta);
            interacao.dataCriacao = LocalDateTime.now();
            interacao.persist();
            logger.info("Interação automatizada criada para a consulta com id {}", interacao.consulta.idConsulta);
            return interacao;
        }else
            return interacaoExistente;
    }

    @Transactional
    @Scheduled(every="10s")
    public void enviarLembreteConsulta(){
        List<Consulta> consultasDeAmanha = consultaService.buscarConsultasMarcadasDiaSeguinte();

        for (Consulta consulta : consultasDeAmanha){
            InteracaoAutomatizada interacaoAutomatizada = buscarOuCriarInteracao(consulta);
            if (interacaoAutomatizada.statusInteracao == null){
                String to = "55" + consulta.paciente.telefonePaciente.replaceAll("[^0-9]", "");
                String body = "Olá "+consulta.paciente.nomePaciente+"! Você tem uma consulta agendada amanhã às "+consulta.dataAgenda+"!";
                chatbotService.sendMessage(to, body);

                interacaoAutomatizada.statusInteracao = "Lembrete enviado";
                interacaoAutomatizada.persist();
            }
        }
    }
}
