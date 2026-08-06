package com.barberbook.backend.dto.catalog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BarberRequest(

    @NotBlank(message = "Nome é obrigatório.")
    @Size(
        max = 120,
        message = "Nome deve ter no máximo 120 caracteres."
    )
    String name,

    boolean active

) {
}
