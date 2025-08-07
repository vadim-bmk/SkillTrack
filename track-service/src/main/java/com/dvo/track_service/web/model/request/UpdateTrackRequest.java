package com.dvo.track_service.web.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateTrackRequest {
    private Long userId;
    private Long skillId;
    private String targetLevel;
    private LocalDate deadline;
}
