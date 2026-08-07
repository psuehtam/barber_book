package com.barberbook.backend.service;

import com.barberbook.backend.dto.barbershop.BarberShopRequest;
import com.barberbook.backend.dto.barbershop.BarberShopResponse;
import com.barberbook.backend.entity.BarberShop;
import com.barberbook.backend.repository.BarberShopRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BarberShopService {

    private final BarberShopRepository repository;

    public BarberShopService(
        BarberShopRepository repository
    ) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public BarberShopResponse get() {
        BarberShop shop = repository
            .findById(BarberShop.SINGLE_ID)
            .orElseGet(() -> new BarberShop("BarberBook"));

        return BarberShopResponse.from(shop);
    }

    @Transactional
    public BarberShopResponse save(
        BarberShopRequest request
    ) {
        BarberShop shop = repository
            .findById(BarberShop.SINGLE_ID)
            .orElseGet(() ->
                new BarberShop(request.name().trim())
            );

        shop.updateAddress(
            request.name().trim(),
            request.cep(),
            request.street(),
            request.number().trim(),
            request.neighborhood(),
            request.city(),
            request.state()
        );

        return BarberShopResponse.from(
            repository.save(shop)
        );
    }
}
