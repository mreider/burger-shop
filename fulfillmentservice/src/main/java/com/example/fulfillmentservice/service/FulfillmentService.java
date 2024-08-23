package com.example.fulfillmentservice.service;

import com.example.fulfillmentservice.model.Order;
import com.example.fulfillmentservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class FulfillmentService {

    private static final Logger logger = LoggerFactory.getLogger(FulfillmentService.class);

    @Autowired
    private OrderRepository orderRepository;

    public void fulfillOrder(Order order) {
        order.setFulfilled(true);
        orderRepository.save(order);
        logger.info("Order fulfilled: {}", order.getId());
    }
}