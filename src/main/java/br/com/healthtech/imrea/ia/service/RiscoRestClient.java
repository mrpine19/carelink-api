package br.com.healthtech.imrea.ia.service;

import br.com.healthtech.imrea.ia.dto.RiscoRequestDTO;
import br.com.healthtech.imrea.ia.dto.RiscoResponseDTO;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/predict_risk")
@RegisterRestClient(configKey="risco-api")
public interface RiscoRestClient {

    @POST
    RiscoResponseDTO predictRisk(RiscoRequestDTO request);

}