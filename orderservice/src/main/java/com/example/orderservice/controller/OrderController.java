package com.example.orderservice.controller;

import com.example.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @PostMapping("/")
    public ResponseEntity<String> createOrder(@RequestParam int quantity) {
        logger.info("Received order with quantity: {}", quantity);
        return orderService.placeOrder(quantity);
    }
}
