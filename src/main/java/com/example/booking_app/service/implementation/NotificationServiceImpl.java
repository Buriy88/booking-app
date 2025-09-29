package com.example.booking_app.service.implementation;

import com.example.booking_app.config.TelegramProperties;
import com.example.booking_app.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final TelegramClient telegramClient;

    private final TelegramProperties props;

    @Override
    public void sendNotification(String message) {
        SendMessage req = SendMessage.builder()
            .chatId(props.chatId())
            .text(message)
            .disableWebPagePreview(true)
            .build();

        try {
            telegramClient.execute(req);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send Telegram message", e);
        }
    }
}
