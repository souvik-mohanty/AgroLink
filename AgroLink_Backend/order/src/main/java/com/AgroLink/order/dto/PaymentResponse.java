package com.AgroLink.order.dto;

import lombok.Data;

@Data
public class PaymentResponse {
    private String razorpayOrderId;
    private String currency;
    private int amount;
}
