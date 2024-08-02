package com.example.burgershop.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
@Service
public class FronEndService {

    private final RestTemplate restTemplate;

    public FronEndService(){
        this.restTemplate = new RestTemplate();
    }

    public ResponseEntity<String> submitOrderToOrderService(int quantity) {
        String orderServiceUrl = "http://localhost:8080/orders";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("quantity", String.valueOf(quantity));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        return restTemplate.postForEntity(orderServiceUrl, request, String.class);
    }
}
