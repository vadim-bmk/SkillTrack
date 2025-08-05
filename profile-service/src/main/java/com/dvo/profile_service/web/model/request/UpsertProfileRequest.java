package com.dvo.profile_service.web.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpsertProfileRequest {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Skill ID is required")
    private Long skillId;

    @NotNull(message = "Level is required")
    private String level;
}
