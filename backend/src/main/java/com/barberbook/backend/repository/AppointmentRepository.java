package com.barberbook.backend.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.barberbook.backend.entity.Appointment;
import com.barberbook.backend.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AppointmentRepository
    extends JpaRepository<Appointment, Long> {

    @Query("""
        select a
        from Appointment a
        join fetch a.barber
        join fetch a.serviceItem
        where a.client.id = :clientId
        order by a.startAt desc
        """)
    List<Appointment> findDetailedByClientId(
        @Param("clientId") Long clientId
    );

    Optional<Appointment> findByCheckoutSessionId(
        String sessionId
    );

    @Query("""
        select a
        from Appointment a
        where a.barber.id = :barberId
          and a.startAt < :rangeEnd
          and a.endAt > :rangeStart
          and (
              a.status = :confirmed
              or (
                  a.status = :pending
                  and a.expiresAt > :now
              )
          )
        """)
    List<Appointment> findBlockingAppointments(
        @Param("barberId") Long barberId,
        @Param("rangeStart") LocalDateTime rangeStart,
        @Param("rangeEnd") LocalDateTime rangeEnd,
        @Param("now") LocalDateTime now,
        @Param("confirmed") AppointmentStatus confirmed,
        @Param("pending") AppointmentStatus pending
    );

    @Query("""
        select (count(a) > 0)
        from Appointment a
        where a.barber.id = :barberId
          and a.id <> :ignoredId
          and a.startAt < :newEnd
          and a.endAt > :newStart
          and (
              a.status = :confirmed
              or (
                  a.status = :pending
                  and a.expiresAt > :now
              )
          )
        """)
    boolean existsBlockingOverlapExcluding(
        @Param("barberId") Long barberId,
        @Param("ignoredId") Long ignoredId,
        @Param("newStart") LocalDateTime newStart,
        @Param("newEnd") LocalDateTime newEnd,
        @Param("now") LocalDateTime now,
        @Param("confirmed") AppointmentStatus confirmed,
        @Param("pending") AppointmentStatus pending
    );
}
