package com.assigment.authservice.annotation;

import com.assigment.authservice.validator.AdvanceLoginValidator;
import com.assigment.authservice.validator.AdvanceRegisterValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = {
        AdvanceRegisterValidator.class,
        AdvanceLoginValidator.class
})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AdvanceRequestValidation {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
