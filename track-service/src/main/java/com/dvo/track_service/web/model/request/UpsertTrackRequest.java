package com.dvo.track_service.web.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpsertTrackRequest {
    @NotNull(message = "User id is required")
    private Long userId;

    @NotNull(message = "Skill id is required")
    private Long skillId;

    @NotBlank(message = "Target level is required")
    private String targetLevel;

    @NotNull(message = "Deadline is required")
    private LocalDate deadline;
}
