package com.dvo.notification_service.service.impl;

import com.dvo.notification_service.entity.NotificationSettings;
import com.dvo.notification_service.event.ProfileEvent;
import com.dvo.notification_service.event.ProgressEvent;
import com.dvo.notification_service.event.TrackEvent;
import com.dvo.notification_service.event.UserEvent;
import com.dvo.notification_service.excpetion.EntityNotFoundException;
import com.dvo.notification_service.repository.NotificationSettingsRepository;
import com.dvo.notification_service.service.NotificationService;
import com.dvo.notification_service.service.TelegramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationSettingsRepository repository;
    private final TelegramService telegramService;


    @Override
    @Transactional
    public void save(UserEvent userEvent) {
        log.info("Call save in NotificationServiceImpl with UserEvent: {}", userEvent);

        if (repository.existsByUserId(userEvent.getUserId())){
            throw new EntityNotFoundException(MessageFormat.format("Notification settings with user ID: {0} already exists", userEvent.getUserId()));
        }

        NotificationSettings settings = NotificationSettings.builder()
                .userId(userEvent.getUserId())
                .telegramId(userEvent.getTelegramId())
                .build();
        repository.save(settings);

        if (userEvent.getTelegramId()!=null){
            telegramService.sendMessage(Long.valueOf(userEvent.getTelegramId()), "Вы добавлены в список уведомлений");
        }
    }

    @Override
    @Transactional
    public void update(UserEvent userEvent) {
        log.info("Call update in NotificationServiceImpl with ID: {}, userEven: {}", userEvent.getUserId(), userEvent);

        NotificationSettings settings = repository.findByUserId(userEvent.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Notification settings with user ID: {0} not found", userEvent.getUserId())));

        settings.setTelegramId(userEvent.getTelegramId());

        repository.save(settings);

        if (!userEvent.getTelegramId().isEmpty()){
            telegramService.sendMessage(Long.valueOf(userEvent.getTelegramId()), "У вас обновлены настройки уведомлений");
        }
    }

    @Override
    @Transactional
    public void deleteByUserId(Long userId) {
        log.info("Call deleteByUserId in NotificationService with userId: {}", userId);
        repository.deleteByUserId(userId);
    }

    @Override
    public void sendProfileToUser(ProfileEvent profileEvent, String text) {
        log.info("Call sendProfileToUser in NotificationService with profileEvent: {}", profileEvent);

        NotificationSettings settings = repository.findByUserId(profileEvent.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Notification settings with user ID: {0} not found", profileEvent.getUserId())));

        telegramService.sendMessage(Long.valueOf(settings.getTelegramId()), text);
    }

    @Override
    public void sendProgressToUser(ProgressEvent progressEvent, String text) {
        log.info("Call sendProgressToUser in NotificationService with profileEvent: {}", progressEvent);

        NotificationSettings settings = repository.findByUserId(progressEvent.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Notification settings with user ID: {0} not found", progressEvent.getUserId())));

        telegramService.sendMessage(Long.valueOf(settings.getTelegramId()), text);
    }

    @Override
    public void sendTrackToUser(TrackEvent trackEvent, String text) {
        log.info("Call sendTrackToUser in NotificationService with profileEvent: {}", trackEvent);

        NotificationSettings settings = repository.findByUserId(trackEvent.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Notification settings with user ID: {0} not found", trackEvent.getUserId())));

        telegramService.sendMessage(Long.valueOf(settings.getTelegramId()), text);
    }


}
