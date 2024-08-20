package com.example.orderservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrderRepository;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routing-key}")
    private String routingKey;

    public ResponseEntity<String> placeOrder(int quantity) {
        Order order = new Order();
        order.setQuantity(quantity);
        order = orderRepository.save(order);

        try {
            String orderJson = objectMapper.writeValueAsString(order);
            rabbitTemplate.convertAndSend(exchange, routingKey, orderJson);
            return ResponseEntity.ok("Order placed with ID: " + order.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to serialize and send order");
        }
    }
}