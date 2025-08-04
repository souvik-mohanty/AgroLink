package com.agrolink.advisory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class AdvisoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdvisoryApplication.class, args);
	}

}
