package com.agrolink.warehouse.service;

import com.agrolink.warehouse.dto.WarehouseRequest;
import com.agrolink.warehouse.dto.WarehouseResponse;
import com.agrolink.warehouse.model.WarehouseEntry;
import com.agrolink.warehouse.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository repository;

    public WarehouseResponse storeCrop(WarehouseRequest request) {
        WarehouseEntry entry = WarehouseEntry.builder()
                .cropName(request.getCropName())
                .farmerId(request.getFarmerId())
                .warehouseLocation(request.getWarehouseLocation())
                .quantity(request.getQuantity())
                .entryTime(LocalDateTime.now())
                .readyForDelivery(false)
                .build();

        return mapToResponse(repository.save(entry));
    }

    public List<WarehouseResponse> getByLocation(String location) {
        return repository.findByWarehouseLocation(location)
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public WarehouseResponse markReadyForDelivery(String id) {
        WarehouseEntry entry = repository.findById(id).orElseThrow();
        entry.setReadyForDelivery(true);
        entry.setExitTime(LocalDateTime.now());
        return mapToResponse(repository.save(entry));
    }

    public List<WarehouseResponse> getReadyForDelivery() {
        return repository.findByReadyForDeliveryTrue()
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private WarehouseResponse mapToResponse(WarehouseEntry entry) {
        return WarehouseResponse.builder()
                .id(entry.getId())
                .cropName(entry.getCropName())
                .farmerId(entry.getFarmerId())
                .warehouseLocation(entry.getWarehouseLocation())
                .quantity(entry.getQuantity())
                .entryTime(entry.getEntryTime())
                .exitTime(entry.getExitTime())
                .readyForDelivery(entry.isReadyForDelivery())
                .build();
    }
}
