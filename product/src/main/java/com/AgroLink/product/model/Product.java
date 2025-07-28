package com.AgroLink.product.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    private String id;
    private String farmerId;
    private String name;
    private String category; // vegetables, grains, tools, fertilizers
    private double pricePerUnit;
    private int quantityAvailable;
    private List<String> imageUrls;
    private String qualityTag; // e.g., Organic, Grade A
    private String cropInfo;
}
