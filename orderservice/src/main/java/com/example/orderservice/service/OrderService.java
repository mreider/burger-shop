package com.example.orderservice.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrderRepository;

@Service
public class OrderService {

    private final ObjectMapper objectMapper;
    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String routingKey;

    @Autowired
    public OrderService(ObjectMapper objectMapper, OrderRepository orderRepository, RabbitTemplate rabbitTemplate,
                        @Value("${rabbitmq.exchange}") String exchange, @Value("${rabbitmq.routing-key}") String routingKey) {
        this.objectMapper = objectMapper;
        this.orderRepository = orderRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    public ResponseEntity<String> placeOrder(int quantity) {
        Order order = new Order();
        order.setQuantity(quantity);
        order = orderRepository.save(order);
        if (new Random().nextInt(100) + 1 > 97) {
            try {
                pullFromInventory();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Server ran out of memory");
            }
        }
        try {
            String orderJson = objectMapper.writeValueAsString(order);
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
            Message message = new Message(orderJson.getBytes(), messageProperties);
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
            return ResponseEntity.ok("Order placed with ID: " + order.getId());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to serialize and send order");
        }
    }
    private void pullFromInventory() {
        List<Object> list = new ArrayList<>();
        while (true) {
            list.add(new Object());
        }
    }
}