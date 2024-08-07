package com.example.fulfillmentservice.service;

import com.example.fulfillmentservice.model.Order;
import com.example.fulfillmentservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class FullfillmentService {
    @Autowired
    private OrderRepository orderRepository;

    @Async
    public CompletableFuture<Void> fulfillOrder(Long orderId) {
        try {
            Thread.sleep(500); // Simulate delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        CompletableFuture.runAsync(() -> {
            Order order = orderRepository.findById(orderId).orElse(null);
            if (order != null) {
                order.setFulfilled(true);
                orderRepository.save(order);
            }
        });

        return CompletableFuture.completedFuture(null);
    }
}
