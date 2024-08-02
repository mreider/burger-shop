package com.example.fulfillmentservice.controller;

import com.example.fulfillmentservice.service.FullfillmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.concurrent.CompletableFuture;

@CrossOrigin(origins = "http://localhost:8082")

@RestController
public class FulfillmentController {
    @Autowired
    private FullfillmentService fulfillmentService;

    @PostMapping("/fulfill")
    public ResponseEntity<String> fulfillOrder(@RequestBody Long orderId) {
        CompletableFuture<Void> future = fulfillmentService.fulfillOrder(orderId);
        future.whenComplete((result, error) -> {
            if (error != null) {

            }
        });
        return ResponseEntity.ok("Fulfillment initiated");
    }
}
