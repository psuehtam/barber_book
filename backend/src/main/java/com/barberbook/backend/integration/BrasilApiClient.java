package com.barberbook.backend.integration;

import com.barberbook.backend.dto.integration.BrasilApiCepResponse;
import com.barberbook.backend.exception.BadRequestException;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class BrasilApiClient {

    private final RestClient restClient;

    public BrasilApiClient(RestClient brasilApiRestClient) {
        this.restClient = brasilApiRestClient;
    }

    public BrasilApiCepResponse findByCep(String rawCep) {
        String cep = rawCep.replaceAll("\\D", "");

        if (!cep.matches("\\d{8}")) {
            throw new BadRequestException(
                "CEP deve conter 8 números."
            );
        }

        try {
            BrasilApiCepResponse response = restClient
                .get()
                .uri("/api/cep/v1/{cep}", cep)
                .retrieve()
                .onStatus(
                    status -> status.value() == 404,
                    (request, result) -> {
                        throw new BadRequestException(
                            "CEP não encontrado."
                        );
                    }
                )
                .onStatus(
                    HttpStatusCode::isError,
                    (request, result) -> {
                        throw new BadRequestException(
                            "Não foi possível consultar o CEP agora."
                        );
                    }
                )
                .body(BrasilApiCepResponse.class);

            if (response == null) {
                throw new BadRequestException(
                    "Resposta vazia da BrasilAPI."
                );
            }

            return response;
        } catch (BadRequestException exception) {
            throw exception;
        } catch (RestClientException exception) {
            throw new BadRequestException(
                "BrasilAPI indisponível. Tente novamente."
            );
        }
    }
}
