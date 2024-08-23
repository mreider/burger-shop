package com.example.fulfillmentservice.listener;

import com.example.fulfillmentservice.model.Order;
import com.example.fulfillmentservice.service.FulfillmentService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class OrderListener {

    private static final Logger logger = LoggerFactory.getLogger(OrderListener.class);

    @Autowired
    private FulfillmentService fulfillmentService;

    @RabbitListener(queues = "${rabbitmq.queue}", containerFactory = "rabbitListenerContainerFactory")
    public void receiveOrder(Order order) {
        logger.info("Received order to fulfill: {}", order);
        fulfillmentService.fulfillOrder(order);
    }
}