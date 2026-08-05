package com.barberbook.backend.repository;

import java.util.List;

import com.barberbook.backend.entity.ServiceItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceItemRepository
    extends JpaRepository<ServiceItem, Long> {

    List<ServiceItem> findByActiveTrueOrderByNameAsc();
}
