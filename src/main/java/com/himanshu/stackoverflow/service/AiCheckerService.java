package com.himanshu.stackoverflow.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AiCheckerService {

    public double checkAi(String text) {
        String url = "https://api.sapling.ai/api/v1/aidetect";
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("key", "BN4XQU9RFH8FV8ONMWAP7J3K1SF9R94Y");
        requestBody.put("text", text);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
        Map<String, Object> responseBody = response.getBody();
        return (double) responseBody.get("score");
    }
}