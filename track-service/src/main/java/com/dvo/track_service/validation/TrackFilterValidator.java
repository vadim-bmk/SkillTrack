package com.dvo.track_service.validation;

import com.dvo.track_service.web.filter.TrackFilter;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.ObjectUtils;

public class TrackFilterValidator implements ConstraintValidator<TrackFilterValid, TrackFilter> {
    @Override
    public boolean isValid(TrackFilter trackFilter, ConstraintValidatorContext constraintValidatorContext) {
        return !ObjectUtils.anyNull(trackFilter.getPageNumber(), trackFilter.getPageSize());
    }
}
