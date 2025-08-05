package com.dvo.skill_service.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SkillResponse {
    private Long id;
    private String name;
    private String area;
    private String level;
}
