package com.barberbook.backend.controller;

import com.barberbook.backend.dto.barbershop.BarberShopRequest;
import com.barberbook.backend.dto.barbershop.BarberShopResponse;
import com.barberbook.backend.dto.integration.BrasilApiCepResponse;
import com.barberbook.backend.integration.BrasilApiClient;
import com.barberbook.backend.service.BarberShopService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BarberShopController {

    private final BarberShopService barberShopService;
    private final BrasilApiClient brasilApiClient;

    public BarberShopController(
        BarberShopService barberShopService,
        BrasilApiClient brasilApiClient
    ) {
        this.barberShopService = barberShopService;
        this.brasilApiClient = brasilApiClient;
    }

    @GetMapping("/barbearia")
    public BarberShopResponse getShop() {
        return barberShopService.get();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/integracoes/cep/{cep}")
    public BrasilApiCepResponse getCep(
        @PathVariable String cep
    ) {
        return brasilApiClient.findByCep(cep);
    }

    @PutMapping("/admin/barbearia")
    public BarberShopResponse saveShop(
        @Valid @RequestBody BarberShopRequest request
    ) {
        return barberShopService.save(request);
    }
}
