package com.example.orderservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")  // Ensure you have a 'test' profile setup if needed
public class OrderserviceApplicationTests {

	@Test
	void contextLoads() {
	}
}