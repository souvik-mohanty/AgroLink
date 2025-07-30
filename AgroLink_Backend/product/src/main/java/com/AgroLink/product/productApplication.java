package com.AgroLink.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients(basePackages = "com.AgroLink.product.feign")
public class productApplication {
    public static void main(String[] args) {
        SpringApplication.run(productApplication.class, args);
    }
}