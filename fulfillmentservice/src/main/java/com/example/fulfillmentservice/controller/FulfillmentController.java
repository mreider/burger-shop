package com.example.fulfillmentservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.concurrent.CompletableFuture;

@CrossOrigin(origins = "http://localhost:8082")

@RestController
public class FulfillmentController {

    @GetMapping("/health")
    public ResponseEntity<String> checkHealth() {
        return ResponseEntity.ok("Fulfillment Service is up and running!");
    }
}
