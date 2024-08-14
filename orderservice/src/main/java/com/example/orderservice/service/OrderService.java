package com.example.orderservice.service;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
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


/*
@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

//    @Value("${fulfillmentservice.url}")
//    private String fulfillmentServiceUrl;

//    private RestTemplate restTemplate = new RestTemplate();
    private Tracer tracer = GlobalOpenTelemetry.getTracer("OrderServiceTracer");

    public ResponseEntity<String> placeOrder(int quantity) {
        Span span = tracer.spanBuilder("Place Order").startSpan();
//        ResponseEntity<String> response;

        try (Scope scope = span.makeCurrent()) {
            // Save the new order
            Order order = new Order();
            order.setQuantity(quantity);
            order = orderRepository.save(order);

            // Randomly trigger a memory leak
            if (new Random().nextInt(100) + 1 > 97) {
//                try {
//                    pullFromInventory();
//                } catch (OutOfMemoryError e) {
//                    e.printStackTrace();
//                    return ResponseEntity.status(500).body("Server ran out of memory");
//                }
                pullFromInventory();
            }

            // Send message to RabbitMQ instead of making a HTTP request
            rabbitTemplate.convertAndSend(exchange, routingKey, order);

            return ResponseEntity.ok("Order placed with ID: " + order.getId());

            // post to fulfillmentservice
//            ResponseEntity<String> fulfillmentResponse = restTemplate.postForEntity(fulfillmentServiceUrl, order.getId(), String.class);
//
//            if (fulfillmentResponse.getStatusCode().is2xxSuccessful()){
//                response = ResponseEntity.ok("Order successfully placed and fulfilled with ID: " + order.getId());
//            } else {
//                response = ResponseEntity.status(500).body("Failed to fulfill order");
//            }
        } catch (OutOfMemoryError e) {
            span.recordException(e);
            span.end();
            return ResponseEntity.status(500).body("Server ran out of memory");
        } finally {
            span.end();
        }

//        return response;
    }

//    private void pullFromInventory() {
//        List<Object> list = new ArrayList<>();
//        while (true) {
//            list.add(new Object());
//        }
//    }
    private  void pullFromInventory() {
        List<Object> leakyList = new ArrayList<>();
        try {
            while (true) {
                byte[] block = new byte[1024 * 1024];   // allocate 1 MB blocks
                leakyList.add(block);

                // add a small delay
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread interrupted");
        }
    }
}
*/

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public ResponseEntity<String> placeOrder(int quantity) {
        Order order = new Order();
        order.setQuantity(quantity);
        order = orderRepository.save(order);

        rabbitTemplate.convertAndSend(exchange, routingKey, order);

        return ResponseEntity.ok("Order placed with ID: " + order.getId());
    }
}