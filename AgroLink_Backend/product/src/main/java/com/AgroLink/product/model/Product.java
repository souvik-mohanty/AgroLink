package com.AgroLink.product.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("products")
public class Product {
    @Id
    private String id;
    private String farmerId;
    private String name;
    private String category;
    private Double pricePerUnit;
    private Integer quantityAvailable;
    private List<String> imageUrls;
    private String qualityTag;
    private String cropInfo;
}
