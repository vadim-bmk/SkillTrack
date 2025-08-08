package com.dvo.progress_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ProgressServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProgressServiceApplication.class, args);
	}

}
