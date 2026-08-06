package com.barberbook.backend.repository;

import com.barberbook.backend.entity.StripeEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StripeEventRepository
    extends JpaRepository<StripeEvent, Long> {

    boolean existsByEventId(String eventId);
}
