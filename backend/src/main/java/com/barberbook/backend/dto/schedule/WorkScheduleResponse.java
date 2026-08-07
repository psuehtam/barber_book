package com.barberbook.backend.dto.schedule;

import java.time.DayOfWeek;
import java.time.LocalTime;

import com.barberbook.backend.entity.WorkSchedule;

public record WorkScheduleResponse(
    Long id,
    DayOfWeek dayOfWeek,
    LocalTime startTime,
    LocalTime endTime
) {

    public static WorkScheduleResponse from(
        WorkSchedule schedule
    ) {
        return new WorkScheduleResponse(
            schedule.getId(),
            schedule.getDayOfWeek(),
            schedule.getStartTime(),
            schedule.getEndTime()
        );
    }
}
