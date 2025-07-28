package com.AgroLink.order.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private String productId;
    private String buyerId;
    private int quantity;
    private double amount;
}
