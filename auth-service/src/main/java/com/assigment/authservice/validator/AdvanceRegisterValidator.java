package com.assigment.authservice.validator;

import com.assigment.authservice.annotation.AdvanceRequestValidation;
import com.assigment.authservice.dto.request.RegisterRequest;
import com.assigment.authservice.util.ConstantUtil;
import com.tracking.commonservice.service.UserServiceRpc;
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
public class AdvanceRegisterValidator implements ConstraintValidator<AdvanceRequestValidation, RegisterRequest> {
    @DubboReference
    private UserServiceRpc userServiceRpc;
    private final MessageSource messageSource;

    @Override
    public boolean isValid(RegisterRequest registerRequest, ConstraintValidatorContext constraintValidatorContext) {
        boolean valid = true;
        String username = registerRequest.getUsername();
        String email = registerRequest.getEmail();
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
