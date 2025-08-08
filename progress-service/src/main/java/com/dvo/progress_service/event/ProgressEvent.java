package com.dvo.progress_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgressEvent {
    private Long id;
    private Long trackId;
    private LocalDateTime createdAt;
    private Boolean approved;
    private String action;
}
