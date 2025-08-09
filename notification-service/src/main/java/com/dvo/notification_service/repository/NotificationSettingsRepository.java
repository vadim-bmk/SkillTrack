package com.dvo.notification_service.repository;

import com.dvo.notification_service.entity.NotificationSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationSettingsRepository extends JpaRepository<NotificationSettings, Long> {
    Boolean existsByUserId(Long userId);
    Optional<NotificationSettings> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}
