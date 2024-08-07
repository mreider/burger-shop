package com.example.burgershop.controller;

import com.example.burgershop.service.FronEndService;
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
    private FronEndService fronEndService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/submitOrder")
    @ResponseBody
    public ResponseEntity<String> submitOrder(@RequestParam("quantity") int quantity) {
        return fronEndService.submitOrderToOrderService(quantity);
    }
}


