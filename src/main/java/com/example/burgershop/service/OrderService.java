package com.example.burgershop.service;

import com.example.burgershop.model.Order;
import com.example.burgershop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    private RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<String> placeOrder(int quantity) {
        // Save the new order
        Order order = new Order();
        order.setQuantity(quantity);
        order = orderRepository.save(order);

        // Post to FulfillmentService
        String fulfillmentServiceUrl = "http://localhost:8080/fulfill";
        ResponseEntity<String> fulfillmentResponse = restTemplate.postForEntity(fulfillmentServiceUrl, order.getId(), String.class);

        if (fulfillmentResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok("Order successfully placed and fulfilled with ID: " + order.getId());
        } else {
            return ResponseEntity.status(500).body("Failed to fulfill order");
        }
    }
}
