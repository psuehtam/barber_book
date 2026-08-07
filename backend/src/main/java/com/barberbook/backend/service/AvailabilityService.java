package com.barberbook.backend.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.barberbook.backend.dto.availability.AvailableSlotResponse;
import com.barberbook.backend.entity.Appointment;
import com.barberbook.backend.entity.AppointmentStatus;
import com.barberbook.backend.entity.Barber;
import com.barberbook.backend.entity.ServiceItem;
import com.barberbook.backend.entity.WorkSchedule;
import com.barberbook.backend.exception.BadRequestException;
import com.barberbook.backend.exception.NotFoundException;
import com.barberbook.backend.repository.AppointmentRepository;
import com.barberbook.backend.repository.BarberRepository;
import com.barberbook.backend.repository.ServiceItemRepository;
import com.barberbook.backend.repository.WorkScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AvailabilityService {

    private final BarberRepository barberRepository;
    private final ServiceItemRepository serviceRepository;
    private final WorkScheduleRepository scheduleRepository;
    private final AppointmentRepository appointmentRepository;
    private final SlotGenerator slotGenerator;
    private final Clock clock;

    public AvailabilityService(
        BarberRepository barberRepository,
        ServiceItemRepository serviceRepository,
        WorkScheduleRepository scheduleRepository,
        AppointmentRepository appointmentRepository,
        SlotGenerator slotGenerator,
        Clock clock
    ) {
        this.barberRepository = barberRepository;
        this.serviceRepository = serviceRepository;
        this.scheduleRepository = scheduleRepository;
        this.appointmentRepository = appointmentRepository;
        this.slotGenerator = slotGenerator;
        this.clock = clock;
    }

    @Transactional(readOnly = true)
    public List<AvailableSlotResponse> find(
        Long barberId,
        Long serviceId,
        LocalDate date
    ) {
        Barber barber = barberRepository
            .findById(barberId)
            .filter(Barber::isActive)
            .orElseThrow(() ->
                new NotFoundException(
                    "Barbeiro ativo não encontrado."
                )
            );

        ServiceItem service = serviceRepository
            .findById(serviceId)
            .filter(ServiceItem::isActive)
            .orElseThrow(() ->
                new NotFoundException(
                    "Serviço ativo não encontrado."
                )
            );

        LocalDate today = LocalDate.now(clock);

        if (date.isBefore(today)) {
            throw new BadRequestException(
                "A data não pode estar no passado."
            );
        }

        WorkSchedule schedule = scheduleRepository
            .findByBarberIdAndDayOfWeek(
                barber.getId(),
                date.getDayOfWeek()
            )
            .orElse(null);

        if (schedule == null) {
            return List.of();
        }

        LocalDateTime dayStart = date.atStartOfDay();
        LocalDateTime dayEnd =
            date.atTime(LocalTime.MAX);
        LocalDateTime now = LocalDateTime.now(clock);

        List<Appointment> blocking =
            appointmentRepository.findBlockingAppointments(
                barberId,
                dayStart,
                dayEnd,
                now,
                AppointmentStatus.CONFIRMED,
                AppointmentStatus.PENDING_PAYMENT
            );

        return slotGenerator.generate(
            date,
            schedule.getStartTime(),
            schedule.getEndTime(),
            service.getDurationMinutes(),
            now,
            blocking
        );
    }
}
