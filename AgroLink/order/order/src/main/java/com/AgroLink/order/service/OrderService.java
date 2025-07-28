package com.AgroLink.order.service;

import com.AgroLink.order.dto.OrderRequest;
import com.AgroLink.order.dto.PaymentResponse;
import com.AgroLink.order.enums.OrderStatus;
import com.AgroLink.order.model.Order;
import com.AgroLink.order.repository.OrderRepository;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RazorpayClient razorpayClient;

    public PaymentResponse createOrder(OrderRequest request) throws RazorpayException {
        // Convert amount to paise (multiply by 100 and round to nearest integer)
        int amountInPaise = (int) Math.round(request.getAmount() * 100);

        // Prepare Razorpay order request
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "txn_" + System.currentTimeMillis());

        // Create order in Razorpay
        com.razorpay.Order razorpayOrder = razorpayClient.orders.create(orderRequest);

        // Save order in database
        Order order = new Order();
        order.setProductId(request.getProductId());
        order.setBuyerId(request.getBuyerId());
        order.setQuantity(request.getQuantity());
        order.setAmount(request.getAmount());
        order.setStatus(OrderStatus.PENDING); // ✅ Fixed: use enum
        order.setRazorpayOrderId(razorpayOrder.get("id"));
        orderRepository.save(order);

        // Build and return response
        PaymentResponse response = new PaymentResponse();
        response.setRazorpayOrderId(razorpayOrder.get("id"));
        response.setCurrency(razorpayOrder.get("currency"));
        response.setAmount(razorpayOrder.get("amount"));

        return response;
    }
}
