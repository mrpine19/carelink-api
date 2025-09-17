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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
                DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm");
                String dataFormatada = consulta.dataAgenda.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().format(formatterData);
                String horaFormatada = consulta.dataAgenda.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().format(formatterHora);

                // Construção da mensagem completa
                String to = "55" + consulta.paciente.telefonePaciente.replaceAll("[^0-9]", "");
                String body = "Olá " + consulta.paciente.nomePaciente + "!\n\n" +
                        "Este é um lembrete da sua teleconsulta agendada com o(a) " + consulta.profissional.nomeProfissional + " do IMREA.\n\n" +
                        "Detalhes da sua consulta:\n" +
                        "- Data: *" + dataFormatada + "*\n" +
                        "- Horário: *" + horaFormatada + "*\n\n" +
                        "Amanhã, 1 hora antes do horário, enviaremos outro lembrete. Em caso de dúvidas, nossa equipe está aqui para te ajudar.";
                chatbotService.sendMessage(to, body);

                interacaoAutomatizada.statusInteracao = "Lembrete enviado";
                interacaoAutomatizada.persist();
            }
        }
    }
}
