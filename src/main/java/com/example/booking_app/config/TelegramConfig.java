package com.example.booking_app.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
@EnableConfigurationProperties(TelegramProperties.class)
public class TelegramConfig {
    @Bean
    TelegramClient telegramClient(TelegramProperties props) {
        return new OkHttpTelegramClient(props.token());
    }
}
