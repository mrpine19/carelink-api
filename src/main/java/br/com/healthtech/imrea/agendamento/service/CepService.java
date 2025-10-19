package br.com.healthtech.imrea.agendamento.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@ApplicationScoped
public class CepService {
    private static final Logger logger = LoggerFactory.getLogger(CepService.class);

    public String obterBairroPaciente(String cep){
        String url = String.format("https://viacep.com.br/ws/%s/json/", cep);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "Java HTTP Client")
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() == 200){
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> jsonMap = mapper.readValue(response.body(), new TypeReference<>() {});
                String bairro = (String) jsonMap.get("bairro");
                logger.info("Encontrado o barro {} para o cep {}", bairro, cep);
                return bairro;
            }else {
                logger.info("Informações do cep {} não encontradas", cep);
                return null;
            }
        }catch (Exception e){
            logger.error("Erro: {}", e.getMessage());
            throw new IllegalArgumentException(("Erro: " + e.getMessage()));
        }
    }
}
