package com.dvo.skill_service.web.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpsertSkillRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Area is required")
    private String area;

    @NotNull(message = "Level is required")
    private List<String> levels;
}
