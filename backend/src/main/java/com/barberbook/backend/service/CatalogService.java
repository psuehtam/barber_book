package com.barberbook.backend.service;

import java.util.List;

import com.barberbook.backend.dto.catalog.BarberRequest;
import com.barberbook.backend.dto.catalog.BarberResponse;
import com.barberbook.backend.dto.catalog.ServiceItemRequest;
import com.barberbook.backend.dto.catalog.ServiceItemResponse;
import com.barberbook.backend.entity.Barber;
import com.barberbook.backend.entity.ServiceItem;
import com.barberbook.backend.exception.ApiException;
import com.barberbook.backend.repository.BarberRepository;
import com.barberbook.backend.repository.ServiceItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CatalogService {

    private final BarberRepository barberRepository;
    private final ServiceItemRepository serviceItemRepository;

    public CatalogService(
        BarberRepository barberRepository,
        ServiceItemRepository serviceItemRepository
    ) {
        this.barberRepository = barberRepository;
        this.serviceItemRepository = serviceItemRepository;
    }

    @Transactional(readOnly = true)
    public List<BarberResponse> listActiveBarbers() {
        return barberRepository.findByActiveTrueOrderByNameAsc()
            .stream()
            .map(BarberResponse::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<ServiceItemResponse> listActiveServices() {
        return serviceItemRepository.findByActiveTrueOrderByNameAsc()
            .stream()
            .map(ServiceItemResponse::from)
            .toList();
    }

    @Transactional
    public BarberResponse createBarber(BarberRequest request) {
        Barber barber = new Barber(request.name().trim());

        if (!request.active()) {
            barber.update(request.name().trim(), false);
        }

        return BarberResponse.from(
            barberRepository.save(barber)
        );
    }

    @Transactional
    public BarberResponse updateBarber(
        Long id,
        BarberRequest request
    ) {
        Barber barber = barberRepository.findById(id)
            .orElseThrow(() -> new ApiException(
                HttpStatus.NOT_FOUND,
                "BARBER_NOT_FOUND",
                "Barbeiro não encontrado."
            ));

        barber.update(
            request.name().trim(),
            request.active()
        );

        return BarberResponse.from(barber);
    }

    @Transactional
    public ServiceItemResponse createService(
        ServiceItemRequest request
    ) {
        validateDuration(request.durationMinutes());

        ServiceItem item = new ServiceItem(
            request.name().trim(),
            request.price(),
            request.durationMinutes()
        );

        if (!request.active()) {
            item.update(
                request.name().trim(),
                request.price(),
                request.durationMinutes(),
                false
            );
        }

        return ServiceItemResponse.from(
            serviceItemRepository.save(item)
        );
    }

    @Transactional
    public ServiceItemResponse updateService(
        Long id,
        ServiceItemRequest request
    ) {
        validateDuration(request.durationMinutes());

        ServiceItem item = serviceItemRepository.findById(id)
            .orElseThrow(() -> new ApiException(
                HttpStatus.NOT_FOUND,
                "SERVICE_NOT_FOUND",
                "Serviço não encontrado."
            ));

        item.update(
            request.name().trim(),
            request.price(),
            request.durationMinutes(),
            request.active()
        );

        return ServiceItemResponse.from(item);
    }

    private void validateDuration(int durationMinutes) {
        if (durationMinutes % 30 != 0) {
            throw new ApiException(
                HttpStatus.BAD_REQUEST,
                "INVALID_SERVICE_DURATION",
                "A duração deve ser múltipla de 30 minutos."
            );
        }
    }
}
