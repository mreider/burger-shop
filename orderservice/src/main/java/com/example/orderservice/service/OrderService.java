package com.example.orderservice.service;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;

import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;


@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Value("${fulfillmentservice.url}")
    private String fulfillmentServiceUrl;

    private RestTemplate restTemplate = new RestTemplate();
    private Tracer tracer = GlobalOpenTelemetry.getTracer("OrderServiceTracer");

    public ResponseEntity<String> placeOrder(int quantity) {
        Span span = tracer.spanBuilder("Place Order").startSpan();
        ResponseEntity<String> response;

        try (Scope scope = span.makeCurrent()) {
            // Save the new order
            Order order = new Order();
            order.setQuantity(quantity);
            order = orderRepository.save(order);

            // Randomly trigger a memory leak
            if (new Random().nextInt(100) + 1 > 97) {
                try {
                    createMemoryLeak();
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                    return ResponseEntity.status(500).body("Server ran out of memory");
                }
            }

            // post to fulfillmentservice
            ResponseEntity<String> fulfillmentResponse = restTemplate.postForEntity(fulfillmentServiceUrl, order.getId(), String.class);

            if (fulfillmentResponse.getStatusCode().is2xxSuccessful()){
                response = ResponseEntity.ok("Order successfully placed and fulfilled with ID: " + order.getId());
            } else {
                response = ResponseEntity.status(500).body("Failed to fulfill order");
            }
        } finally {
            span.end();
        }

        return response;

        /*
        // Save the new order
        Order order = new Order();
        order.setQuantity(quantity);
        order = orderRepository.save(order);

        // Post to FulfillmentService
        ResponseEntity<String> fulfillmentResponse = restTemplate.postForEntity(fulfillmentServiceUrl, order.getId(), String.class);

        if (fulfillmentResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok("Order successfully placed and fulfilled with ID: " + order.getId());
        } else {
            return ResponseEntity.status(500).body("Failed to fulfill order");
        }
        */
    }

    private void createMemoryLeak() {
        List<Object> list = new ArrayList<>();
        while (true) {
            list.add(new Object());
        }
    }
}
