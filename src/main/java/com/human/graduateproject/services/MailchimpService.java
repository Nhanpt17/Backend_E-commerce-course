package com.human.graduateproject.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class MailchimpService {

    @Value("${mailchimp.api.key}")
    private String apiKey;

    @Value("${mailchimp.list.id}")
    private String listId;

    @Value("${mailchimp.server.prefix}")
    private String serverPrefix;

    private final RestTemplate restTemplate = new RestTemplate();

    public String addSubscriber(String email) {
        String url = String.format("https://%s.api.mailchimp.com/3.0/lists/%s/members", serverPrefix, listId);

        // Dữ liệu gửi cho Mailchimp
        Map<String, Object> body = new HashMap<>();
        body.put("email_address", email);
        body.put("status", "subscribed"); // hoặc "pending" nếu bạn bật xác nhận qua email

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth("anystring", apiKey); // Mailchimp yêu cầu Basic Auth

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            return response.getBody();
        } catch (Exception e) {
            return "{\"error\":\"" + e.getMessage() + "\"}";
        }
    }
}

