package br.com.healthtech.imrea.ia.service;

import br.com.healthtech.imrea.consulta.domain.Consulta;
import br.com.healthtech.imrea.consulta.service.ConsultaService;
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
import java.util.List;

@ApplicationScoped
public class ScoreDeRiscoService {
    private static final Logger logger = LoggerFactory.getLogger(ScoreDeRiscoService.class);

    @Inject
    @RestClient
    RiscoRestClient riscoRestClient;

    @Inject
    ConsultaService consultaService;

    public int calculaScoreDeRisco(Paciente paciente, String especialidadeConsulta) {
        try {
            RiscoRequestDTO requestDTO = new RiscoRequestDTO();
            LocalDate hoje = LocalDate.now();

            requestDTO.setIdadePaciente(Period.between(paciente.getDataNascimentoPaciente(), hoje).getYears());

            requestDTO.setBairroPaciente(paciente.getBairroPaciente());
            requestDTO.setTemCuidador(paciente.getCuidadores().isEmpty() ? 0 : 1);
            requestDTO.setEspecialidadeConsulta(especialidadeConsulta);

            requestDTO.setAfinidadeDigitalScore(paciente.getAfinidadeDigital());
            requestDTO.setFaltasConsecutivasHistorico(paciente.getNumeroFaltasConsecutivas()); // Assuming correct getter
            requestDTO.setTaxaAbsenteismoHistorica(calcularTaxaAbsenteismoHistorica(paciente.getIdPaciente()));

            int tempoDesdePrimeiraConsultaDias = paciente.getDataPrimeiraConsulta() == null ? 0 :
                    Period.between(paciente.getDataPrimeiraConsulta(), LocalDate.now()).getDays();
            requestDTO.setTempoDesdePrimeiraConsultaDias(tempoDesdePrimeiraConsultaDias);

            if (tempoDesdePrimeiraConsultaDias == 0) {
                requestDTO.setTempoDesdeUltimaConsultaDias(0);
            } else {
                Consulta consulta = consultaService.buscarConsultaMaisRecenteRealizada(paciente.getIdPaciente());
                int tempoDesdeUltimaConsultaDias = consulta == null ? tempoDesdePrimeiraConsultaDias :
                        Period.between(consulta.getDataAgenda().toLocalDate(), LocalDate.now()).getDays();
                requestDTO.setTempoDesdeUltimaConsultaDias(tempoDesdeUltimaConsultaDias);
            }

            logger.info("Enviando requisição para API de Risco para o paciente {}", paciente.getNomePaciente());
            RiscoResponseDTO response = riscoRestClient.predictRisk(requestDTO);

            int score = response.getScoreRiscoCarelink();
            logger.info("Score de risco recebido para o paciente {}: {}", paciente.getNomePaciente(), score);

            return score;

        } catch (Exception e) {
            logger.error("Erro ao calcular score de risco para o paciente {}: {}", paciente.getNomePaciente(), e.getMessage(), e);
            return 0;
        }
    }

    public float calcularTaxaAbsenteismoHistorica(Long idPaciente) {
        float taxaAbsenteismoHistorica = 0.0f;
        int qtdDeFaltas = 0;

        List<Consulta> consultasPassadas = consultaService.buscarConsultasPassadasParaTaxa(idPaciente);

        for (Consulta consulta : consultasPassadas) {
            if ("PACIENTE NAO COMPARECEU".equalsIgnoreCase(consulta.getStatusConsulta())) {
                qtdDeFaltas += 1;
            }
        }

        if (!consultasPassadas.isEmpty()) {
            taxaAbsenteismoHistorica = (float) qtdDeFaltas / consultasPassadas.size();
        }

        return taxaAbsenteismoHistorica;
    }
}
