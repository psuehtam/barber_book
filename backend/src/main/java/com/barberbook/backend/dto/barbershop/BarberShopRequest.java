package com.barberbook.backend.dto.barbershop;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record BarberShopRequest(
    @NotBlank
    @Size(max = 120)
    String name,

    @Pattern(
        regexp = "\\d{8}",
        message = "CEP deve conter 8 números."
    )
    String cep,

    @Size(max = 160)
    String street,

    @NotBlank
    @Size(max = 20)
    String number,

    @Size(max = 120)
    String neighborhood,

    @Size(max = 120)
    String city,

    @Pattern(
        regexp = "[A-Z]{2}",
        message = "Estado deve ter duas letras."
    )
    String state
) {
}
