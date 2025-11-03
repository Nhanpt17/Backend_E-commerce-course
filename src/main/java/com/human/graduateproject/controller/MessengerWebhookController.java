package com.human.graduateproject.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/webhook")
public class WebhookController {

    @Value("${messenger.verify-token:MY_VERIFY_TOKEN}")
    private String verifyToken;

    @GetMapping("/ok")
    public ResponseEntity<String> ok() {
        return ResponseEntity.ok("ok");
    }

    // Endpoint verify cho Messenger (GET)
    @GetMapping
    public ResponseEntity<String> verify(
            @RequestParam(name = "hub.mode", required = false) String mode,
            @RequestParam(name = "hub.verify_token", required = false) String token,
            @RequestParam(name = "hub.challenge", required = false) String challenge
    ) {
        // Yêu cầu của Meta: phải trả challenge khi token hợp lệ
        if ("subscribe".equals(mode) && verifyToken.equals(token)) {
            return ResponseEntity.ok(challenge != null ? challenge : "");
        }
        return ResponseEntity.status(403).build();
    }
}
