package com.barberbook.backend.dto.appointment;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record CreateAppointmentRequest(
    @NotNull Long barberId,
    @NotNull Long serviceId,
    @NotNull
    @Future(message = "O horário deve estar no futuro.")
    LocalDateTime startAt
) {
}
