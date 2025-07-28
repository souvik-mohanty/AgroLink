package com.AgroLink.product.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private String id;
    private String name;
    private String category;
    private double pricePerUnit;
    private int quantityAvailable;
    private List<String> imageUrls;
    private String qualityTag;
    private String cropInfo;
}
