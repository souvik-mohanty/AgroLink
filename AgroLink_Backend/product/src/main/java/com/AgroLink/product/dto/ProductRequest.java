package com.AgroLink.product.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

@Data
public class ProductRequest {
    @NotBlank(message = "Farmer ID cannot be empty")
    private String farmerId;

    @NotBlank(message = "Product name cannot be empty")
    private String name;

    @NotBlank(message = "Category cannot be empty")
    private String category;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be positive")
    private Double pricePerUnit;

    @NotNull(message = "Quantity cannot be null")
    @Positive(message = "Quantity must be a positive number")
    private Integer quantityAvailable;

    private List<String> photoIds;
    private String qualityTag;
    private String cropInfo;
}