package com.barberbook.backend.dto.catalog;

import com.barberbook.backend.entity.Barber;

public record BarberResponse(
    Long id,
    String name,
    boolean active
) {

    public static BarberResponse from(Barber barber) {
        return new BarberResponse(
            barber.getId(),
            barber.getName(),
            barber.isActive()
        );
    }
}
