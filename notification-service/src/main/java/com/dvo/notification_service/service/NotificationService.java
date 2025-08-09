package com.dvo.notification_service.service;

import com.dvo.notification_service.event.ProfileEvent;
import com.dvo.notification_service.event.ProgressEvent;
import com.dvo.notification_service.event.TrackEvent;
import com.dvo.notification_service.event.UserEvent;

public interface NotificationService {
    void save(UserEvent userEvent);

    void update(UserEvent userEvent);

    void deleteByUserId(Long userId);

    void sendProfileToUser(ProfileEvent profileEvent, String text);

    void sendProgressToUser(ProgressEvent progressEvent, String text);

    void sendTrackToUser(TrackEvent trackEvent, String text);
}
