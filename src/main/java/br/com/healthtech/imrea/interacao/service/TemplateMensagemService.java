package br.com.healthtech.imrea.interacao.service;

import br.com.healthtech.imrea.consulta.domain.Consulta;
import br.com.healthtech.imrea.interacao.domain.TipoInteracao;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

import java.time.format.DateTimeFormatter;


@ApplicationScoped
public class TemplateMensagemService {

    public JsonObject construirMensagem(Consulta consulta, String nomeDestinatario, TipoInteracao tipo) {
        if (tipo == TipoInteracao.LEMBRETE_24H)
            return construirMensagem24HorasConsulta(consulta, nomeDestinatario);
        else if (tipo == TipoInteracao.LEMBRETE_1H)
            return construirMensagem1HoraConsulta(consulta, nomeDestinatario);

        return null;
    }

    private JsonObject construirMensagem24HorasConsulta(Consulta consulta, String nomeDestinatario) {
        String dataFormatada = consulta.getDataAgenda().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String horaFormatada = consulta.getDataAgenda().toLocalTime().toString();

        JsonObject body = Json.createObjectBuilder()
                .add("text", String.format("Olá, %s! Sou o CareLink e estou aqui para garantir que você não perca sua teleconsulta.\n\n" +
                                "A consulta de %s com o(a) Dr(a). %s está agendada para:\n\n" +
                                "📅 Data: *%s*\n⏰ Horário: *%s*\n\n" +
                                "Por favor, confirme abaixo sua presença. Sua resposta nos ajuda a organizar a agenda do hospital!",
                        nomeDestinatario, consulta.getEspecialidade().getNomeEspecialidade(), consulta.getProfissional().getNomeProfissional(),
                        dataFormatada, horaFormatada))
                .build();

        JsonObject footer = Json.createObjectBuilder()
                .add("text", "Responda para garantir seu horário. Seu link e código serão enviados no próximo lembrete.")
                .build();

        JsonArrayBuilder buttonsArrayBuilder = Json.createArrayBuilder()
                .add(Json.createObjectBuilder()
                        .add("type", "quick_reply")
                        .add("title", "✅ Sim, irei comparecer")
                        .add("id", "CONFIRM_PRESENCE_SIM"))
                .add(Json.createObjectBuilder()
                        .add("type", "quick_reply")
                        .add("title", "❌ Preciso reagendar/cancelar")
                        .add("id", "CONFIRM_PRESENCE_NAO"))
                .add(Json.createObjectBuilder()
                        .add("type", "quick_reply")
                        .add("title", "❓ Tenho dúvidas sobre o acesso")
                        .add("id", "CONFIRM_DUVIDA_ACESSO"));

        JsonObject action = Json.createObjectBuilder()
                .add("buttons", buttonsArrayBuilder)
                .build();

        return Json.createObjectBuilder()
                .add("body", body)
                .add("footer", footer)
                .add("action", action)
                .add("type", "button")
                .build();
    }

    private JsonObject construirMensagem1HoraConsulta(Consulta consulta, String nomeDestinatario) {
        String horaFormatada = consulta.getDataAgenda().toLocalTime().toString();

        String body = String.format("🚨 *ATENÇÃO, %s!* 🚨\n\n" +
                "A teleconsulta com o(a) %s (%s) está marcada para *agora, às %s!*\n\n" +
                "Clique no link e use o código para entrar:\n" +
                "🔗 *LINK DE ACESSO:* %s\n" +
                "🔑 *CÓDIGO DE ACESSO:* %s\n\n" +
                "✅ *O que fazer agora?*\n" +
                "1. Clique no link acima.\n" +
                "2. Digite o Código de Acesso.\n\n" +
                "*Precisa de ajuda imediata?* Responda AGORA a esta mensagem com a palavra 'AJUDA' para que nosso assistente possa te auxiliar.",
                nomeDestinatario, consulta.getProfissional().getNomeProfissional(), consulta.getEspecialidade().getNomeEspecialidade(), horaFormatada,
                consulta.getEspecialidade().getLinkConsultaEspecialidade(), consulta.getCodigoConsulta());

        return Json.createObjectBuilder()
                .add("body", body)
                .build();

    }
}
