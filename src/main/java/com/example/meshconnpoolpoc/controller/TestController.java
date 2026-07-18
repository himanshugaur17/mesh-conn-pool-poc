package com.example.meshconnpoolpoc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class TestController {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${ENVOY_URL}")
    private String envoyUrl;

    @GetMapping("/call")
    public String simulate() {
        return restTemplate.getForObject(envoyUrl + "/mock-endpoint", String.class);
    }
}
