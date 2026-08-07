package com.barberbook.backend.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.barberbook.backend.dto.schedule.WorkScheduleRequest;
import com.barberbook.backend.dto.schedule.WorkScheduleResponse;
import com.barberbook.backend.entity.Barber;
import com.barberbook.backend.entity.WorkSchedule;
import com.barberbook.backend.exception.BadRequestException;
import com.barberbook.backend.exception.NotFoundException;
import com.barberbook.backend.repository.BarberRepository;
import com.barberbook.backend.repository.WorkScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkScheduleService {

    private final BarberRepository barberRepository;
    private final WorkScheduleRepository scheduleRepository;

    public WorkScheduleService(
        BarberRepository barberRepository,
        WorkScheduleRepository scheduleRepository
    ) {
        this.barberRepository = barberRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @Transactional(readOnly = true)
    public List<WorkScheduleResponse> list(Long barberId) {
        requireBarber(barberId);

        return scheduleRepository
            .findByBarberIdOrderByDayOfWeek(barberId)
            .stream()
            .map(WorkScheduleResponse::from)
            .toList();
    }

    @Transactional
    public List<WorkScheduleResponse> replace(
        Long barberId,
        List<WorkScheduleRequest> requests
    ) {
        Barber barber = requireBarber(barberId);

        validate(requests);

        scheduleRepository.deleteByBarberId(barberId);
        scheduleRepository.flush();

        List<WorkSchedule> schedules = requests
            .stream()
            .map(item -> new WorkSchedule(
                barber,
                item.dayOfWeek(),
                item.startTime(),
                item.endTime()
            ))
            .toList();

        return scheduleRepository
            .saveAll(schedules)
            .stream()
            .map(WorkScheduleResponse::from)
            .toList();
    }

    private Barber requireBarber(Long barberId) {
        return barberRepository
            .findById(barberId)
            .orElseThrow(() ->
                new NotFoundException(
                    "Barbeiro não encontrado."
                )
            );
    }

    private void validate(
        List<WorkScheduleRequest> requests
    ) {
        Set<java.time.DayOfWeek> seen = new HashSet<>();

        for (WorkScheduleRequest item : requests) {
            if (!item.startTime().isBefore(item.endTime())) {
                throw new BadRequestException(
                    "Hora inicial deve ser anterior à final."
                );
            }

            if (!seen.add(item.dayOfWeek())) {
                throw new BadRequestException(
                    "Existe mais de um horário para o mesmo dia."
                );
            }
        }
    }
}
