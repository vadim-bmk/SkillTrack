package com.dvo.notification_service.service;

public interface TelegramService {
    void sendMessage(Long chatId, String text);
}
