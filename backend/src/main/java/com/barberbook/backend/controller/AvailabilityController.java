package com.barberbook.backend.controller;

import java.time.LocalDate;
import java.util.List;

import com.barberbook.backend.dto.availability.AvailableSlotResponse;
import com.barberbook.backend.service.AvailabilityService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/barbeiros")
public class AvailabilityController {

    private final AvailabilityService service;

    public AvailabilityController(
        AvailabilityService service
    ) {
        this.service = service;
    }

    @GetMapping("/{barberId}/disponibilidade")
    public List<AvailableSlotResponse> find(
        @PathVariable Long barberId,
        @RequestParam Long serviceId,
        @RequestParam
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate date
    ) {
        return service.find(
            barberId,
            serviceId,
            date
        );
    }
}
