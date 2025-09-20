package br.com.healthtech.imrea.paciente.service;

import br.com.healthtech.imrea.agendamento.domain.Consulta;
import br.com.healthtech.imrea.agendamento.service.ConsultaService;
import br.com.healthtech.imrea.alerta.service.ChatbotService;
import br.com.healthtech.imrea.paciente.domain.Cuidador;
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
import java.util.Date;
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
    public void enviarLembreteConsulta() {
        List<Consulta> consultasDeAmanha = consultaService.buscarConsultasMarcadasDiaSeguinte();

        for (Consulta consulta : consultasDeAmanha) {
            InteracaoAutomatizada interacaoAutomatizada = buscarOuCriarInteracao(consulta);
            if (interacaoAutomatizada.statusInteracao == null) {
                String to = normalizarTelefone(consulta.paciente.telefonePaciente);
                String body = construirMensagem(consulta, consulta.paciente.nomePaciente);
                chatbotService.sendMessage(to, body);

                if (!consulta.paciente.cuidadores.isEmpty()) {
                    enviaMensagemCuidador(consulta);
                }

                interacaoAutomatizada.statusInteracao = "Lembrete enviado";
                interacaoAutomatizada.persist();
            }
        }
    }

    public void enviaMensagemCuidador(Consulta consulta) {
        for (Cuidador cuidador : consulta.paciente.cuidadores) {
            String to = normalizarTelefone(cuidador.telefoneCuidador);
            String body = construirMensagem(consulta, cuidador.nomeCuidador);
            chatbotService.sendMessage(to, body);
        }
    }

    private String normalizarTelefone(String numero) {
        return "55" + numero.replaceAll("[^0-9]", "");
    }

    private String construirMensagem(Consulta consulta, String nomeDestinatario) {
        String dataFormatada = formatarData(consulta.dataAgenda);
        String horaFormatada = formatarHora(consulta.dataAgenda);

        return "Olá " + nomeDestinatario + "!\n\n" +
                "Este é um lembrete da sua teleconsulta agendada com o(a) " + consulta.profissional.nomeProfissional + " do IMREA.\n\n" +
                "Detalhes da sua consulta:\n" +
                "- Data: *" + dataFormatada + "*\n" +
                "- Horário: *" + horaFormatada + "*\n\n" +
                "Amanhã, 1 hora antes do horário, enviaremos outro lembrete. Em caso de dúvidas, nossa equipe está aqui para te ajudar.";
    }

    private String formatarData(Date data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return data.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().format(formatter);
    }

    private String formatarHora(Date data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return data.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().format(formatter);
    }
}
