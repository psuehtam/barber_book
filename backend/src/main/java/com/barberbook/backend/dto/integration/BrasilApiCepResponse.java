package com.barberbook.backend.dto.integration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BrasilApiCepResponse(
    String cep,
    String state,
    String city,
    String neighborhood,
    String street
) {
}
