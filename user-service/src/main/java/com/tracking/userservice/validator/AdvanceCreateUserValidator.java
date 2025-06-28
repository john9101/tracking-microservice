package com.tracking.userservice.validator;

import com.tracking.commonservice.service.UserServiceRpc;
import com.tracking.userservice.annotation.AdvanceRequestValidation;
import com.tracking.userservice.dto.request.CreateUserRequest;
import com.tracking.userservice.util.ConstantUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class AdvanceCreateUserValidator implements ConstraintValidator<AdvanceRequestValidation, CreateUserRequest> {
    @DubboReference
    private UserServiceRpc userServiceRpc;
    private final MessageSource messageSource;

    @Override
    public boolean isValid(CreateUserRequest request, ConstraintValidatorContext constraintValidatorContext) {
        boolean valid = true;
        String username = request.getUsername();
        Locale locale = LocaleContextHolder.getLocale();

        if (username != null && userServiceRpc.existUsername(username)) {
            String message = messageSource.getMessage(ConstantUtil.CONSTRAINT_USERNAME_EXISTED, new Object[]{username}, locale);
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("username")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            valid = false;
        }

//        if (email != null && userServiceRpc.existEmail(email)) {
//            String message = messageSource.getMessage(ConstantUtil.CONSTRAINT_EMAIL_EXISTED, new Object[]{email}, locale);
//            constraintValidatorContext
//                    .buildConstraintViolationWithTemplate(message)
//                    .addPropertyNode("email")
//                    .addConstraintViolation()
//                    .disableDefaultConstraintViolation();
//            valid = false;
//        }

        return valid;
    }
}
