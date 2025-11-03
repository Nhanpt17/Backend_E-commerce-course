package com.human.graduateproject.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.human.graduateproject.messenger.MessengerService;

import java.util.Map;

@RestController
@RequestMapping("/api/webhook")
public class MessengerWebhookController {

    @Value("${messenger.verify-token:MY_VERIFY_TOKEN}")
    private String verifyToken;

    private final MessengerService messengerService;

    // Constructor injection
    public MessengerWebhookController(MessengerService messengerService) {
        this.messengerService = messengerService;
    }

    // 1) VERIFY: Meta gửi GET để xác minh webhook
    @GetMapping
    public ResponseEntity<String> verify(
            @RequestParam(name = "hub.mode", required = false) String mode,
            @RequestParam(name = "hub.verify_token", required = false) String token,
            @RequestParam(name = "hub.challenge", required = false) String challenge
    ) {
        if ("subscribe".equals(mode) && verifyToken.equals(token)) {
            return ResponseEntity.ok(challenge != null ? challenge : "");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Verification failed");
    }

    // 2) RECEIVE: Meta gửi POST khi có message/sự kiện
    @PostMapping
    public ResponseEntity<Void> receive(@RequestBody Map<String, Object> payload,
                                        @RequestHeader Map<String, String> headers) {
        // Log cho dễ debug
        System.out.println("=== Messenger Webhook payload ===");
        System.out.println(payload);
        // GỌI SERVICE xử lý
        messengerService.handleWebhook(payload);
        return ResponseEntity.ok().build();
    }

    // 3) Health check đơn giản
    @GetMapping("/ok")
    public String ok() { return "ok"; }
}
