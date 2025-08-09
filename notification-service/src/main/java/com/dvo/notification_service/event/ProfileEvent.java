package com.dvo.notification_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileEvent {
    private Long id;
    private Long userId;
    private Long skillId;
    private String skillName;
    private Boolean verified;
    private String action;
}
