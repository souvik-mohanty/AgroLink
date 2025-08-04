package com.AgroLink.order.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "product-service")
public interface ProductClients {

}
