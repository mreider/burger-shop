package com.example.frontendservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
@Service
public class FrontEndService {

    private final RestTemplate restTemplate;

    @Value("${orderservice.url}")
    private String orderServiceUrl;

    public FrontEndService(){
        System.out.println("The order URL: " + orderServiceUrl);
        this.restTemplate = new RestTemplate();
    }

    public ResponseEntity<String> submitOrderToOrderService(int quantity) {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("quantity", String.valueOf(quantity));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        System.out.println("Submitting order to URL: " + orderServiceUrl);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        return restTemplate.postForEntity(orderServiceUrl, request, String.class);
    }
}
