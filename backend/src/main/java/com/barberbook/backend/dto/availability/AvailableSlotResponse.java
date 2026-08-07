package com.barberbook.backend.dto.availability;

import java.time.LocalDateTime;

public record AvailableSlotResponse(
    LocalDateTime startAt,
    LocalDateTime endAt
) {
}
