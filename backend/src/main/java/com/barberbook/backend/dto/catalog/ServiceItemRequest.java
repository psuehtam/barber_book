package com.barberbook.backend.dto.catalog;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ServiceItemRequest(

    @NotBlank(message = "Nome é obrigatório.")
    @Size(
        max = 120,
        message = "Nome deve ter no máximo 120 caracteres."
    )
    String name,

    @NotNull(message = "Preço é obrigatório.")
    @DecimalMin(
        value = "0.01",
        message = "Preço deve ser maior que zero."
    )
    BigDecimal price,

    @Min(
        value = 30,
        message = "Duração mínima é 30 minutos."
    )
    @Max(
        value = 480,
        message = "Duração máxima é 480 minutos."
    )
    int durationMinutes,

    boolean active

) {
}
