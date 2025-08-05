package com.dvo.skill_service.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SkillFilterValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SkillFilterValid {
    String message() default "Поля для пагинации должны быть указаны!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
