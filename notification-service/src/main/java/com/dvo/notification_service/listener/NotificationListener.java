package com.dvo.notification_service.listener;

import com.dvo.notification_service.event.ProfileEvent;
import com.dvo.notification_service.event.ProgressEvent;
import com.dvo.notification_service.event.TrackEvent;
import com.dvo.notification_service.event.UserEvent;
import com.dvo.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationListener {
    private final NotificationService notificationService;

    @KafkaListener(topics = "user-topic", groupId = "notification-group", containerFactory = "userEventKafkaListener")
    public void listenUserEvent(UserEvent event) {
        String action = event.getAction();
        Long userId = event.getUserId();
        String telegramId = event.getTelegramId();

        switch (action) {
            case "create" -> {
                log.info("Notification: создан клиент {} c telegram ID {}", userId, telegramId);
                notificationService.save(event);
            }

            case "update" -> {
                log.info("Notification: обновлен клиент {} c telegram ID {}", userId, telegramId);
                notificationService.update(event);
            }

            case "delete" -> {
                log.info("Notification: удален клиент {} c telegram ID {}", userId, telegramId);
                notificationService.deleteByUserId(userId);
            }

            default -> log.warn("Notification: неизвестное действие {} для клиента {}", action, userId);
        }
    }

    @KafkaListener(topics = "profile-topic", groupId = "notification-group", containerFactory = "profileEventKafkaListener")
    public void listenProfileEvent(ProfileEvent event) {
        String action = event.getAction();
        Long profileId = event.getId();
        Long userId = event.getUserId();
        Long skillId = event.getSkillId();
        String skillName = event.getSkillName();
        Boolean verified = event.getVerified();
        String textVerified = event.getVerified() ? "подтвержден" : "не подтвержден";

        switch (action) {
            case "создан" -> {
                log.info("Notification profile: создан профиль {} с навыком {} для клиента {}", profileId, skillName, userId);
                notificationService.sendProfileToUser(event, String.format("В вашем профиле добавлен навык %s, данный навык %s", skillName, textVerified));
            }

            case "обновлен" -> {
                log.info("Notification profile: обновлен профиль {} с навыком {} для клиента {}", skillName, skillName, userId);
                notificationService.sendProfileToUser(event, String.format("В вашем профиле обновлен навык %s, данный навык %s", skillName, textVerified));

            }

            case "изменено подтверждение" -> {
                log.info("Notification profile: изменено подтверждение навыка {} для клиента {}. Навык {}.", skillName, userId, textVerified);
                notificationService.sendProfileToUser(event, String.format("В вашем профиле изменено подтверждение навыка %s, данный навык %s", skillName, textVerified));

            }

            default -> log.warn("Notification profile: неизвестное действие {} для клиента {}", action, userId);
        }
    }

    @KafkaListener(topics = "progress-topic", groupId = "notification-group", containerFactory = "progressEventKafkaListener")
    public void listenProgressEvent(ProgressEvent event) {
        Long progressId = event.getId();
        Long trackId = event.getTrackId();
        LocalDateTime createdAt = event.getCreatedAt();
        String textApproved = event.getApproved() ? "подтвержден" : "не подтвержден";
        String action = event.getAction();
        Long userId = event.getUserId();

        switch (action) {
            case "создан" -> {
                log.info("Notification progress: создан прогресс {} для трека {}. Уведомляем клиента {}", progressId, trackId, userId);
                notificationService.sendProgressToUser(event, String.format("Ваш прогресс %s создан к треку %s, данный прогресс %s", progressId, trackId, textApproved));
            }

            case "обновлен" -> {
                log.info("Notification progress: обновлен прогресс {} для трека {}. Уведомляем клиента {}", progressId, trackId, userId);
                notificationService.sendProgressToUser(event, String.format("Ваш прогресс %s обновлен к треку %s, данный прогресс %s", progressId, trackId, textApproved));
            }

            case "изменен статус" -> {
                log.info("Notification progress: изменено подтверждение прогресса {} для трека {}. Уведомляем клиента {}", progressId, trackId, userId);
                notificationService.sendProgressToUser(event, String.format("Вашем прогрессе %s изменено подтверждение к треку %s, данный прогресс %s", progressId, trackId, textApproved));
            }

            default -> log.warn("Notification progress: неизвестное действие {} для клиента {}", action, userId);
        }
    }

    @KafkaListener(topics = "track-topic", groupId = "notification-group", containerFactory = "trackEventKafkaListener")
    public void listenTrackEvent(TrackEvent event) {
        Long trackId = event.getId();
        Long userId = event.getUserId();
        Long skillId = event.getSkillId();
        String targetLevel = event.getTargetLevel();
        LocalDate deadline = event.getDeadline();
        String status = event.getStatus();
        LocalDateTime createdAt = event.getCreatedAt();
        LocalDateTime updatedAt = event.getUpdatedAt();
        String action = event.getAction();

        switch (action) {
            case "создан" -> {
                log.info("Notification track: создан трек {} для клиента {}", trackId, userId);
                notificationService.sendTrackToUser(event, String.format("Создан трек %s с уровнем %s в %s для навыка %s, срок прохождения %s, статус %s", trackId, targetLevel, createdAt.toString(), skillId, deadline, status));
            }

            case "изменен" -> {
                log.info("Notification track: изменен трек {} для клиента {}", trackId, userId);
                notificationService.sendTrackToUser(event, String.format("Изменен трек %s с уровнем %s в %s для навыка %s, срок прохождения %s, статус %s", trackId, targetLevel, updatedAt.toString(), skillId, deadline, status));
            }

            case "обновлен статус" -> {
                log.info("Notification track: обновлен статус трека {} для клиента {}", trackId, userId);
                notificationService.sendTrackToUser(event, String.format("Обновлен статус трека %s с уровнем %s в %s для навыка %s, срок прохождения %s, статус %s", trackId, targetLevel, updatedAt.toString(), skillId, deadline, status));
            }

            default -> log.warn("Notification track: неизвестное действие {} для клиента {}", action, userId);
        }
    }
}
