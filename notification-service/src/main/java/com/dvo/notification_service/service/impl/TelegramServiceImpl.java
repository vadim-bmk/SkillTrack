package com.dvo.notification_service.service.impl;

import com.dvo.notification_service.service.TelegramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramServiceImpl implements TelegramService {
    @Value("${telegram.bot.token}")
    private String botToken;

    private final RestTemplate restTemplate;

    @Override
    public void sendMessage(Long chatId, String text) {
        log.info("Call sendMessage in TelegramServiceImpl with chatId: {}, message: {}", chatId, text);

        String url = String.format("https://api.telegram.org/bot%s/sendMessage", botToken);

        Map<String, Object> params = Map.of(
                "chat_id", chatId,
                "text", text
        );

        restTemplate.postForObject(url, params, String.class);
    }
}
