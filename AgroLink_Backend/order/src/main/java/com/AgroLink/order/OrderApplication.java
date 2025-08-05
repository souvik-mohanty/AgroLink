package com.AgroLink.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class OrderApplication {
	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);
	}

}

// TODO: MAKE A ORDER SERVICE TO EXECUTE THE ORDER OF CART BUY OR A SINGLE PRODUCT
// TODO: Implement Circuit breaker
// TODO: Implement Rate Limiter
// TODO: Implement Heart beat algo in eureka server