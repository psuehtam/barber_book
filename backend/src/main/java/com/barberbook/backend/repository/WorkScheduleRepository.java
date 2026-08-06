package com.barberbook.backend.repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

import com.barberbook.backend.entity.WorkSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkScheduleRepository
    extends JpaRepository<WorkSchedule, Long> {

    Optional<WorkSchedule> findByBarberIdAndDayOfWeek(
        Long barberId,
        DayOfWeek dayOfWeek
    );

    List<WorkSchedule> findByBarberIdOrderByDayOfWeek(
        Long barberId
    );

    void deleteByBarberId(Long barberId);
}
