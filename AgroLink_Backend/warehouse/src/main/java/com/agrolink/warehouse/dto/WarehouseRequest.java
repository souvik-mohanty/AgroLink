package com.agrolink.warehouse.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseRequest {
    private String cropName;
    private String farmerId;
    private String warehouseLocation;
    private double quantity;
}
