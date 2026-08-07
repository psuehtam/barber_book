package com.barberbook.backend.controller;

import java.util.List;

import com.barberbook.backend.dto.schedule.WorkScheduleRequest;
import com.barberbook.backend.dto.schedule.WorkScheduleResponse;
import com.barberbook.backend.service.WorkScheduleService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    "/api/admin/barbeiros/{barberId}/horarios"
)
public class WorkScheduleController {

    private final WorkScheduleService service;

    public WorkScheduleController(
        WorkScheduleService service
    ) {
        this.service = service;
    }

    @GetMapping
    public List<WorkScheduleResponse> list(
        @PathVariable Long barberId
    ) {
        return service.list(barberId);
    }

    @PutMapping
    public List<WorkScheduleResponse> replace(
        @PathVariable Long barberId,
        @Valid
        @RequestBody List<WorkScheduleRequest> requests
    ) {
        return service.replace(barberId, requests);
    }
}
