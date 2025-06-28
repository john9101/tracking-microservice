package com.assigment.authservice.validator;

import com.assigment.authservice.annotation.AdvanceRequestValidation;
import com.assigment.authservice.dto.request.LoginRequest;
import com.assigment.authservice.util.ConstantUtil;
import com.tracking.commonservice.dto.UserDtoRpc;
import com.tracking.commonservice.service.UserServiceRpc;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class AdvanceLoginValidator implements ConstraintValidator<AdvanceRequestValidation, LoginRequest> {
    @DubboReference
    private UserServiceRpc userServiceRpc;
    private final MessageSource messageSource;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean isValid(LoginRequest loginRequest, ConstraintValidatorContext constraintValidatorContext) {
        boolean valid = true;
        String username = loginRequest.getUsername();
        Locale locale = LocaleContextHolder.getLocale();
        UserDtoRpc userDtoRpc = userServiceRpc.getUser(username);
        if (userDtoRpc == null) {
            String message = messageSource.getMessage(ConstantUtil.CONSTRAINT_USERNAME_NOT_FOUND, new Object[]{username}, locale);
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("username")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            valid = false;
        }else if(!passwordEncoder.matches(loginRequest.getPassword(), userDtoRpc.getPassword())) {
            String message = messageSource.getMessage(ConstantUtil.CONTRAINT_PASSWORD_INCORRECT, new Object[]{username}, locale);
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("password")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            valid = false;
        }
        return valid;
    }
}
