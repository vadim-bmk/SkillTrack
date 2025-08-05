package com.dvo.skill_service.web.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpsertSkillRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Area is required")
    private String area;

    @NotBlank(message = "Level is required")
    private String level;
}
