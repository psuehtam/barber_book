package com.barberbook.backend.dto.appointment;

import java.time.LocalDateTime;

import com.barberbook.backend.entity.AppointmentStatus;

public record CheckoutResponse(
    Long appointmentId,
    AppointmentStatus status,
    String checkoutUrl,
    LocalDateTime expiresAt
) {
}
