package com.dvo.progress_service.validation;

import com.dvo.progress_service.web.filter.ProgressFilter;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.ObjectUtils;

public class ProgressFilterValidator implements ConstraintValidator<ProgressFilterValid, ProgressFilter> {
    @Override
    public boolean isValid(ProgressFilter progressFilter, ConstraintValidatorContext constraintValidatorContext) {
        return !ObjectUtils.anyNull(progressFilter.getPageNumber(), progressFilter.getPageSize());
    }
}
