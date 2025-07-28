package com.agrolink.warehouse.controller;

import com.agrolink.warehouse.dto.WarehouseRequest;
import com.agrolink.warehouse.dto.WarehouseResponse;
import com.agrolink.warehouse.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouse")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService service;

    @PostMapping("/store")
    public WarehouseResponse storeCrop(@RequestBody WarehouseRequest request) {
        return service.storeCrop(request);
    }

    @GetMapping("/location/{location}")
    public List<WarehouseResponse> getByLocation(@PathVariable String location) {
        return service.getByLocation(location);
    }

    @PutMapping("/mark-ready/{id}")
    public WarehouseResponse markReadyForDelivery(@PathVariable String id) {
        return service.markReadyForDelivery(id);
    }

    @GetMapping("/ready")
    public List<WarehouseResponse> getReadyForDelivery() {
        return service.getReadyForDelivery();
    }
}
