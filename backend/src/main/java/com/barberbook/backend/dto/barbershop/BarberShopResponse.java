package com.barberbook.backend.dto.barbershop;

import com.barberbook.backend.entity.BarberShop;

public record BarberShopResponse(
    Long id,
    String name,
    String cep,
    String street,
    String number,
    String neighborhood,
    String city,
    String state
) {

    public static BarberShopResponse from(BarberShop shop) {
        return new BarberShopResponse(
            shop.getId(),
            shop.getName(),
            shop.getCep(),
            shop.getStreet(),
            shop.getNumber(),
            shop.getNeighborhood(),
            shop.getCity(),
            shop.getState()
        );
    }
}
