package com.dvo.skill_service.web.model.filter;

import com.dvo.skill_service.validation.SkillFilterValid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SkillFilterValid
public class SkillFilter {
    private Integer pageNumber;
    private Integer pageSize;
    private String name;
    private String area;
    private String level;
}
