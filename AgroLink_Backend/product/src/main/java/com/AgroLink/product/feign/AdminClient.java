package com.AgroLink.product.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "admin-service")
public interface AdminClient {

    @GetMapping("/api/admin")
    String adminHello();
}
