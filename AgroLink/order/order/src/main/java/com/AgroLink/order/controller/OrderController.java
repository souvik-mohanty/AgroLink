package com.AgroLink.order.controller;


import com.AgroLink.order.dto.OrderRequest;
import com.AgroLink.order.model.Order;
import com.AgroLink.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // ➕ Create new order
    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest order) {
        try {
            orderService.createOrder(order);
            return ResponseEntity.status(201).body("Order created successfully");
        } catch (Exception e) {
            System.err.println("OrderController Create error: " + e.getMessage());
            return ResponseEntity.status(500).body("Failed to create order");
        }
    }

}
