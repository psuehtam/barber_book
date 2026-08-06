package com.barberbook.backend.controller;

import java.util.List;

import com.barberbook.backend.dto.catalog.BarberResponse;
import com.barberbook.backend.dto.catalog.ServiceItemResponse;
import com.barberbook.backend.service.CatalogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PublicCatalogController {

    private final CatalogService catalogService;

    public PublicCatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/barbeiros")
    public List<BarberResponse> listBarbers() {
        return catalogService.listActiveBarbers();
    }

    @GetMapping("/servicos")
    public List<ServiceItemResponse> listServices() {
        return catalogService.listActiveServices();
    }
}
