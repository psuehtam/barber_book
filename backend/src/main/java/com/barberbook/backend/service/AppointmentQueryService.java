package com.barberbook.backend.service;

import java.util.List;

import com.barberbook.backend.dto.appointment.AppointmentResponse;
import com.barberbook.backend.entity.User;
import com.barberbook.backend.exception.NotFoundException;
import com.barberbook.backend.repository.AppointmentRepository;
import com.barberbook.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppointmentQueryService {

    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;

    public AppointmentQueryService(
        UserRepository userRepository,
        AppointmentRepository appointmentRepository
    ) {
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> listMine(
        String email
    ) {
        User user = userRepository
            .findByEmailIgnoreCase(email)
            .orElseThrow(() ->
                new NotFoundException(
                    "Cliente não encontrado."
                )
            );

        return appointmentRepository
            .findDetailedByClientId(user.getId())
            .stream()
            .map(AppointmentResponse::from)
            .toList();
    }
}
