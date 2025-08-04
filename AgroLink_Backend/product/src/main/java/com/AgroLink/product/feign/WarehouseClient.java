package com.AgroLink.product.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "warehouse-service")
public interface WarehouseClient {
    @GetMapping("/api/warehouse/stock")
    int getStockForProduct(@RequestParam("productId") String productId);

}
