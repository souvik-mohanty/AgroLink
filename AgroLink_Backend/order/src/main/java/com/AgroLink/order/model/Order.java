package com.AgroLink.order.model;

import com.AgroLink.order.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    private String id;

    private String productId;
    private String buyerId;
    private int quantity;
    private double amount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // Enum: PENDING, PAID, CANCELLED

    private String razorpayOrderId;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

