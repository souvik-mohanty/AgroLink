package com.AgroLink.product.dto;

import lombok.*;
import java.util.List;

//sourav:-
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {
    private String farmerId;
    private String name;
    private String category;
    private Double pricePerUnit;
    private Integer quantityAvailable;
    private List<String> imageUrls;
    private String qualityTag;
    private String cropInfo;
}
