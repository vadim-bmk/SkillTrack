package com.dvo.profile_service.validation;

import com.dvo.profile_service.web.filter.ProfileFilter;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.ObjectUtils;

public class ProfileFilterValidator implements ConstraintValidator<ProfileFilterValid, ProfileFilter> {
    @Override
    public boolean isValid(ProfileFilter profileFilter, ConstraintValidatorContext constraintValidatorContext) {
        return !ObjectUtils.anyNull(profileFilter.getPageNumber(), profileFilter.getPageSize());
    }
}
