package br.com.healthtech.imrea.interacao.service;

import br.com.healthtech.imrea.agendamento.domain.Consulta;
import br.com.healthtech.imrea.interacao.domain.TipoInteracao;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TemplateMensagemService {

    public String construirMensagem(Consulta consulta, String nomeDestinatario, TipoInteracao tipo) {
        if (tipo == TipoInteracao.LEMBRETE_24H)
            return construirMensagem24HorasConsulta(consulta, nomeDestinatario);
        else if (tipo == TipoInteracao.LEMBRETE_1H)
            return construirMensagem1HoraConsulta(consulta, nomeDestinatario);

        return "";
    }

    private String construirMensagem24HorasConsulta(Consulta consulta, String nomeDestinatario) {
        String dataFormatada = consulta.getDataAgenda().toLocalDate().toString();
        String horaFormatada = consulta.getDataAgenda().toLocalTime().toString();

        return "Olá " + nomeDestinatario + "!\n\n" +
                "Este é um lembrete da sua teleconsulta agendada de "+consulta.getProfissional().getEspecialidadeProfissional()+" com o(a) " + consulta.getProfissional().getNomeProfissional() + " do IMREA.\n\n" +
                "Detalhes da sua consulta:\n" +
                "Paciente: " + consulta.getPaciente().getNomePaciente() + "\n" +
                "- Data: *" + dataFormatada + "*\n" +
                "- Horário: *" + horaFormatada + "*\n\n" +
                "Amanhã, 1 hora antes do horário, enviaremos outro lembrete com mais informações. Em caso de dúvidas, nossa equipe está aqui para te ajudar.";
    }

    private String construirMensagem1HoraConsulta(Consulta consulta, String nomeDestinatario) {
        String horaFormatada = consulta.getDataAgenda().toLocalTime().toString();

        return "🚨 *ATENÇÃO, " + nomeDestinatario + "!* 🚨\n\n" +
                "Sua teleconsulta com o(a) " + consulta.getProfissional().getNomeProfissional() + " ("+consulta.getProfissional().getEspecialidadeProfissional()+") está marcada para *agora, às " + horaFormatada + "!*\n\n" +
                "Clique no link e use o código para entrar:\n" +
                "🔗 *LINK DE ACESSO:* " + consulta.getLinkConsulta() + "\n" +
                "🔑 *CÓDIGO DE ACESSO:* " + consulta.getCodigoConsulta() + "\n\n" +
                "✅ *O que fazer agora?*\n" +
                "1. Clique no link acima.\n" +
                "2. Digite o Código de Acesso.\n\n" +
                "*Precisa de ajuda imediata?* Responda AGORA a esta mensagem com a palavra 'AJUDA' para que nosso assistente possa te auxiliar.";
    }
}
