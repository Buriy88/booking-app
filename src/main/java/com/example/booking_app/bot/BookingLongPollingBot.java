package com.example.booking_app.bot;

import com.example.booking_app.config.TelegramProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class BookingLongPollingBot implements SpringLongPollingBot,
    LongPollingSingleThreadUpdateConsumer {
    private final TelegramProperties props;

    @Override
    public String getBotToken() {
        return props.token();
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {

    }
}
