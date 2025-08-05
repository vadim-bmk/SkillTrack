package com.dvo.skill_service.validation;

import com.dvo.skill_service.web.model.filter.SkillFilter;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.ObjectUtils;

public class SkillFilterValidator implements ConstraintValidator<SkillFilterValid, SkillFilter> {
    @Override
    public boolean isValid(SkillFilter skillFilter, ConstraintValidatorContext constraintValidatorContext) {
        return !ObjectUtils.anyNull(skillFilter.getPageNumber(), skillFilter.getPageSize());
    }
}
