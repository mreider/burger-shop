package com.example.burgershop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BurgershopApplication {

	public static void main(String[] args) {
		SpringApplication.run(BurgershopApplication.class, args);
	}

}
