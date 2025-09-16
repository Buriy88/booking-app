package com.example.booking_app.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${TELEGRAM_BOT_TOKEN}")
    private String botToken;

    @Value("${TELEGRAM_CHAT_ID}")
    private String chatId;

    @Override
    public void sendNotification(String message) {
        String url = String.format(
            "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s",
            botToken, chatId, message
        );
        restTemplate.getForObject(url, String.class);
    }
}
