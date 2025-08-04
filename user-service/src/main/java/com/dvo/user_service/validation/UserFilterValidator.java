package com.dvo.user_service.validation;

import com.dvo.user_service.web.model.filter.UserFilter;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.ObjectUtils;

public class UserFilterValidator implements ConstraintValidator<UserFilterValid, UserFilter> {
    @Override
    public boolean isValid(UserFilter userFilter, ConstraintValidatorContext constraintValidatorContext) {
        return !ObjectUtils.anyNull(userFilter.getPageNumber(), userFilter.getPageSize());
    }
}
