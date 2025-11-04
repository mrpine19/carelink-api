package br.com.healthtech.imrea.ia.service;

import br.com.healthtech.imrea.agendamento.domain.RegistroAgendamento;
import br.com.healthtech.imrea.ia.dto.RiscoRequestDTO;
import br.com.healthtech.imrea.ia.dto.RiscoResponseDTO;
import br.com.healthtech.imrea.paciente.domain.Paciente;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Period;

@ApplicationScoped
public class ScoreDeRiscoService {
    private static final Logger logger = LoggerFactory.getLogger(ScoreDeRiscoService.class);

    @Inject
    @RestClient
    RiscoRestClient riscoRestClient;

    public int calculaScoreDeRisco(RegistroAgendamento registro, Paciente paciente){
        try {
            RiscoRequestDTO requestDTO = new RiscoRequestDTO();

            requestDTO.setIdadePaciente(90);
            requestDTO.setBairroPaciente("Grajaú");
            requestDTO.setTemCuidador(1);
            requestDTO.setEspecialidadeConsulta("Fisioterapia");

            requestDTO.setAfinidadeDigitalScore(40);
            requestDTO.setFaltasConsecutivasHistorico(1);
            requestDTO.setTaxaAbsenteismoHistorica(0.10);
            requestDTO.setTempoDesdePrimeiraConsultaDias(20);
            requestDTO.setTempoDesdeUltimaConsultaDias(10);

            logger.info("Enviando requisição para API de Risco para o paciente {}", paciente.getNomePaciente());
            RiscoResponseDTO response = riscoRestClient.predictRisk(requestDTO);

            int score = response.getScoreRiscoCarelink();
            logger.info("Score de risco recebido para o paciente {}: {}", paciente.getNomePaciente(), score);

            return score;

        } catch (Exception e) {
            logger.error("Erro ao calcular score de risco para o paciente {}: {}", paciente.getNomePaciente(), e.getMessage(), e);
            // Retorna 0 em caso de erro para não interromper o fluxo principal
            return 0;
        }
    }
}
