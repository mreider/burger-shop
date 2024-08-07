package com.example.frontendservice.controller;

import com.example.frontendservice.service.FrontEndService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FrontEndController {

    @Autowired
    private FrontEndService frontEndService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/submitOrder")
    @ResponseBody
    public ResponseEntity<String> submitOrder(@RequestParam("quantity") int quantity) {
        return frontEndService.submitOrderToOrderService(quantity);
    }
}

