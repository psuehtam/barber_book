package com.barberbook.backend.controller;

import com.barberbook.backend.dto.catalog.BarberRequest;
import com.barberbook.backend.dto.catalog.BarberResponse;
import com.barberbook.backend.dto.catalog.ServiceItemRequest;
import com.barberbook.backend.dto.catalog.ServiceItemResponse;
import com.barberbook.backend.service.CatalogService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminCatalogController {

    private final CatalogService catalogService;

    public AdminCatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @PostMapping("/barbeiros")
    @ResponseStatus(HttpStatus.CREATED)
    public BarberResponse createBarber(
        @Valid @RequestBody BarberRequest request
    ) {
        return catalogService.createBarber(request);
    }

    @PutMapping("/barbeiros/{id}")
    public BarberResponse updateBarber(
        @PathVariable Long id,
        @Valid @RequestBody BarberRequest request
    ) {
        return catalogService.updateBarber(id, request);
    }

    @PostMapping("/servicos")
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceItemResponse createService(
        @Valid @RequestBody ServiceItemRequest request
    ) {
        return catalogService.createService(request);
    }

    @PutMapping("/servicos/{id}")
    public ServiceItemResponse updateService(
        @PathVariable Long id,
        @Valid @RequestBody ServiceItemRequest request
    ) {
        return catalogService.updateService(id, request);
    }
}
