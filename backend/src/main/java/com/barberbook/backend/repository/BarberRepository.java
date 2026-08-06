package com.barberbook.backend.repository;

import java.util.List;
import java.util.Optional;

import com.barberbook.backend.entity.Barber;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BarberRepository
    extends JpaRepository<Barber, Long> {

    List<Barber> findByActiveTrueOrderByNameAsc();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from Barber b where b.id = :id")
    Optional<Barber> findByIdForUpdate(@Param("id") Long id);
}
