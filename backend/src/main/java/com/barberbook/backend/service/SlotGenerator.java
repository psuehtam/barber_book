package com.barberbook.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.barberbook.backend.dto.availability.AvailableSlotResponse;
import com.barberbook.backend.entity.Appointment;
import org.springframework.stereotype.Component;

@Component
public class SlotGenerator {

    private static final int STEP_MINUTES = 30;

    public List<AvailableSlotResponse> generate(
        LocalDate date,
        LocalTime workStart,
        LocalTime workEnd,
        int durationMinutes,
        LocalDateTime now,
        List<Appointment> blocking
    ) {
        List<AvailableSlotResponse> result = new ArrayList<>();
        LocalDateTime candidateStart = date.atTime(workStart);
        LocalDateTime workEndAt = date.atTime(workEnd);

        while (
            !candidateStart
                .plusMinutes(durationMinutes)
                .isAfter(workEndAt)
        ) {
            LocalDateTime slotStart = candidateStart;
            LocalDateTime slotEnd =
                slotStart.plusMinutes(durationMinutes);

            boolean inFuture = slotStart.isAfter(now);

            boolean overlaps = blocking.stream().anyMatch(existing ->
                existing.getStartAt().isBefore(slotEnd)
                    && existing.getEndAt().isAfter(slotStart)
            );

            if (inFuture && !overlaps) {
                result.add(
                    new AvailableSlotResponse(
                        slotStart,
                        slotEnd
                    )
                );
            }

            candidateStart =
                candidateStart.plusMinutes(STEP_MINUTES);
        }

        return result;
    }
}
