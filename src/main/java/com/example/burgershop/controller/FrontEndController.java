package com.example.burgershop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FrontEndController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/submitOrder")
    @ResponseBody
    public ResponseEntity<String> submitOrder(@RequestParam("quantity") int quantity) {
        // Simulate HTTP call to OrderService
        boolean isSuccess = true; // This should actually call another service
        if (isSuccess) {
            return ResponseEntity.ok("Success");
        } else {
            return ResponseEntity.status(500).body("Failed");
        }
    }
}
