package org.agrolink.cart.dto;

import lombok.Data;

@Data
public class AddItemRequest {
    private String productId;
    private int quantity;
}