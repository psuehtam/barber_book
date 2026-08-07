package com.barberbook.backend.service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import com.barberbook.backend.dto.appointment.CreateAppointmentRequest;
import com.barberbook.backend.entity.Appointment;
import com.barberbook.backend.entity.AppointmentStatus;
import com.barberbook.backend.entity.Barber;
import com.barberbook.backend.entity.ServiceItem;
import com.barberbook.backend.entity.User;
import com.barberbook.backend.entity.WorkSchedule;
import com.barberbook.backend.exception.BadRequestException;
import com.barberbook.backend.exception.ConflictException;
import com.barberbook.backend.exception.NotFoundException;
import com.barberbook.backend.repository.AppointmentRepository;
import com.barberbook.backend.repository.BarberRepository;
import com.barberbook.backend.repository.ServiceItemRepository;
import com.barberbook.backend.repository.UserRepository;
import com.barberbook.backend.repository.WorkScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppointmentReservationService {

    private static final int PAYMENT_WINDOW_MINUTES = 30;

    private final BarberRepository barberRepository;
    private final ServiceItemRepository serviceRepository;
    private final UserRepository userRepository;
    private final WorkScheduleRepository scheduleRepository;
    private final AppointmentRepository appointmentRepository;
    private final Clock clock;

    public AppointmentReservationService(
        BarberRepository barberRepository,
        ServiceItemRepository serviceRepository,
        UserRepository userRepository,
        WorkScheduleRepository scheduleRepository,
        AppointmentRepository appointmentRepository,
        Clock clock
    ) {
        this.barberRepository = barberRepository;
        this.serviceRepository = serviceRepository;
        this.userRepository = userRepository;
        this.scheduleRepository = scheduleRepository;
        this.appointmentRepository = appointmentRepository;
        this.clock = clock;
    }

    @Transactional
    public Appointment reserve(
        String clientEmail,
        CreateAppointmentRequest request
    ) {
        LocalDateTime now = LocalDateTime.now(clock);

        User client = userRepository
            .findByEmailIgnoreCase(clientEmail)
            .orElseThrow(() ->
                new NotFoundException(
                    "Cliente não encontrado."
                )
            );

        Barber barber = barberRepository
            .findByIdForUpdate(request.barberId())
            .filter(Barber::isActive)
            .orElseThrow(() ->
                new NotFoundException(
                    "Barbeiro ativo não encontrado."
                )
            );

        ServiceItem service = serviceRepository
            .findById(request.serviceId())
            .filter(ServiceItem::isActive)
            .orElseThrow(() ->
                new NotFoundException(
                    "Serviço ativo não encontrado."
                )
            );

        LocalDateTime start = request.startAt();
        LocalDateTime end = start.plusMinutes(
            service.getDurationMinutes()
        );

        validateStartGrid(start);
        validateFuture(start, now);
        validateInsideWorkSchedule(
            barber,
            start,
            end
        );

        List<Appointment> blocking =
            appointmentRepository.findBlockingAppointments(
                barber.getId(),
                start,
                end,
                now,
                AppointmentStatus.CONFIRMED,
                AppointmentStatus.PENDING_PAYMENT
            );

        if (!blocking.isEmpty()) {
            throw new ConflictException(
                "Este horário acabou de ser ocupado. Escolha outro."
            );
        }

        Appointment appointment = new Appointment(
            client,
            barber,
            service,
            start,
            end,
            service.getPrice(),
            now.plusMinutes(PAYMENT_WINDOW_MINUTES),
            now
        );

        return appointmentRepository.save(appointment);
    }

    private void validateStartGrid(
        LocalDateTime start
    ) {
        if (
            start.getMinute() % 30 != 0
                || start.getSecond() != 0
                || start.getNano() != 0
        ) {
            throw new BadRequestException(
                "O horário deve começar em um intervalo de 30 minutos."
            );
        }
    }

    private void validateFuture(
        LocalDateTime start,
        LocalDateTime now
    ) {
        if (!start.isAfter(now)) {
            throw new BadRequestException(
                "O horário deve estar no futuro."
            );
        }
    }

    private void validateInsideWorkSchedule(
        Barber barber,
        LocalDateTime start,
        LocalDateTime end
    ) {
        WorkSchedule schedule = scheduleRepository
            .findByBarberIdAndDayOfWeek(
                barber.getId(),
                start.getDayOfWeek()
            )
            .orElseThrow(() ->
                new BadRequestException(
                    "O barbeiro não trabalha neste dia."
                )
            );

        LocalDateTime allowedStart = start
            .toLocalDate()
            .atTime(schedule.getStartTime());

        LocalDateTime allowedEnd = start
            .toLocalDate()
            .atTime(schedule.getEndTime());

        if (
            start.isBefore(allowedStart)
                || end.isAfter(allowedEnd)
        ) {
            throw new BadRequestException(
                "O atendimento fica fora do horário de trabalho."
            );
        }
    }
}
