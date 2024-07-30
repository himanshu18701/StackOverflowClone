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
public class PlagiarismCheckerService {

    public int checkPlagiarism(String text) {
        String url = "https://www.prepostseo.com/apis/checkPlag";
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("key", "68de9c4b705d6c07a4e367296d10c773");
        requestBody.put("data", text);
        requestBody.put("ignore", "");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        Map<String, Object> responseBody = response.getBody();
        return (int) responseBody.get("plagPercent");
    }
}