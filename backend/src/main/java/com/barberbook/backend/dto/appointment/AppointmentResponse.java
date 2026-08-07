package com.barberbook.backend.dto.appointment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.barberbook.backend.entity.Appointment;
import com.barberbook.backend.entity.AppointmentStatus;

public record AppointmentResponse(
    Long id,
    String barberName,
    String serviceName,
    LocalDateTime startAt,
    LocalDateTime endAt,
    BigDecimal amount,
    AppointmentStatus status,
    LocalDateTime expiresAt
) {

    public static AppointmentResponse from(
        Appointment appointment
    ) {
        return new AppointmentResponse(
            appointment.getId(),
            appointment.getBarber().getName(),
            appointment.getServiceItem().getName(),
            appointment.getStartAt(),
            appointment.getEndAt(),
            appointment.getAmount(),
            appointment.getStatus(),
            appointment.getExpiresAt()
        );
    }
}
