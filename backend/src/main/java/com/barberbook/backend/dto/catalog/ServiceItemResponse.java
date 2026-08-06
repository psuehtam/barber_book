package com.barberbook.backend.dto.catalog;

import java.math.BigDecimal;

import com.barberbook.backend.entity.ServiceItem;

public record ServiceItemResponse(
    Long id,
    String name,
    BigDecimal price,
    int durationMinutes,
    boolean active
) {

    public static ServiceItemResponse from(ServiceItem item) {
        return new ServiceItemResponse(
            item.getId(),
            item.getName(),
            item.getPrice(),
            item.getDurationMinutes(),
            item.isActive()
        );
    }
}
