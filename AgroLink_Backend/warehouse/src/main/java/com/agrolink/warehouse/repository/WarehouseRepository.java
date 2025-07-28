package com.agrolink.warehouse.repository;

import com.agrolink.warehouse.model.WarehouseEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WarehouseRepository extends MongoRepository<WarehouseEntry, String> {
    List<WarehouseEntry> findByWarehouseLocation(String location);
    List<WarehouseEntry> findByReadyForDeliveryTrue();
}
