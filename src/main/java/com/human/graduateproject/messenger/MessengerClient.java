package com.human.graduateproject.messenger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class MessengerClient {

    @Value("${messenger.page-access-token}")
    private String pageAccessToken;

    @Value("${messenger.graph-base-url:https://graph.facebook.com}")
    private String graphBaseUrl;

    @Value("${messenger.graph-version:v20.0}")
    private String graphVersion;

    private final RestTemplate rest = new RestTemplate();

    public void sendText(String recipientId, String text) {
        String url = buildSendApiUrl();
        Map<String, Object> body = Map.of(
                "recipient", Map.of("id", recipientId),
                "message", Map.of("text", text)
        );
        postJson(url, body);
    }

    /** Hiển thị trạng thái gõ, seen… (không bắt buộc) */
    public void sendSenderAction(String recipientId, String action) {
        String url = buildSendApiUrl();
        Map<String, Object> body = Map.of(
                "recipient", Map.of("id", recipientId),
                "sender_action", action
        );
        postJson(url, body);
    }

    private String buildSendApiUrl() {
        return String.format("%s/%s/me/messages?access_token=%s",
                graphBaseUrl, graphVersion, pageAccessToken);
    }

    private void postJson(String url, Map<String, Object> body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> resp = rest.postForEntity(url, entity, String.class);
            // In log nhẹ nhàng để debug khi demo
            System.out.println("[SendAPI] status=" + resp.getStatusCodeValue() + " body=" + resp.getBody());
        } catch (HttpStatusCodeException ex) {
            System.err.println("[SendAPI][ERROR] status=" + ex.getStatusCode().value()
                    + " body=" + ex.getResponseBodyAsString());
            throw ex;
        }
    }

}
