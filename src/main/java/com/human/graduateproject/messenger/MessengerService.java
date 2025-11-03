package com.human.graduateproject.messenger;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MessengerService {

    private final MessengerClient client;

    public MessengerService(MessengerClient client) {
        this.client = client;
    }

    @SuppressWarnings("unchecked")
    public void handleWebhook(Map<String, Object> payload) {
        if (payload == null) return;
        if (!"page".equals(payload.get("object"))) return;

        List<Map<String, Object>> entries = (List<Map<String, Object>>) payload.get("entry");
        if (entries == null) return;

        for (Map<String, Object> entry : entries) {
            List<Map<String, Object>> messaging = (List<Map<String, Object>>) entry.get("messaging");
            if (messaging == null) continue;

            for (Map<String, Object> event : messaging) {
                Map<String, Object> sender = (Map<String, Object>) event.get("sender");
                Map<String, Object> message = (Map<String, Object>) event.get("message");

                if (message == null) continue;

                // 1) Bỏ qua các echo do chính Page gửi
                Object isEcho = message.get("is_echo");
                if (Boolean.TRUE.equals(isEcho)) {
                    continue;
                }

                // 2) Chỉ xử lý khi có text và sender là user
                if (sender == null || message.get("text") == null) continue;

                String psid = String.valueOf(sender.get("id")); // đây là PSID của user
                String text = String.valueOf(message.get("text"));

                client.sendSenderAction(psid, "typing_on");
                client.sendText(psid, "Bạn vừa nói: " + text);
                client.sendSenderAction(psid, "typing_off");
            }
        }
    }
}
