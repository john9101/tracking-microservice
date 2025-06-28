package com.tracking.taskservice.validator;

import com.tracking.taskservice.annotation.EnumConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<EnumConstraint, String> {
    private Enum<?>[] enumConstants;

    @Override
    public void initialize(EnumConstraint constraintAnnotation) {
        enumConstants = constraintAnnotation.enumClass().getEnumConstants();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) return true;
        for (Enum<?> e : enumConstants) {
            if (e.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
