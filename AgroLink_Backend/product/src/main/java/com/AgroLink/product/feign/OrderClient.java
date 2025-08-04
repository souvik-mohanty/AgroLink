package com.AgroLink.product.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "order-service")
public interface OrderClient {

    @GetMapping("/actuator/health")
    String checkHealth();
}
