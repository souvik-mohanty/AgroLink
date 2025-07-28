package com.agrolink.warehouse.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseResponse {
    private String id;
    private String cropName;
    private String farmerId;
    private String warehouseLocation;
    private double quantity;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private boolean readyForDelivery;
}
