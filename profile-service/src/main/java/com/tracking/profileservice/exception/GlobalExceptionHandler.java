package com.tracking.profileservice.exception;

import com.tracking.profileservice.dto.response.AlternativeResponse;
import com.tracking.profileservice.util.ConstantUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<AlternativeResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, Locale locale) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            String message = messageSource.getMessage(error, locale);
            errors.put(error.getField(), message);
        });

        String message = messageSource.getMessage(ConstantUtil.VALIDATION_INVALID, null, locale);
        AlternativeResponse alternativeResponse = AlternativeResponse.instance(HttpStatus.BAD_REQUEST.value(), message, errors);
        return ResponseEntity.badRequest().body(alternativeResponse);
    }

    @ExceptionHandler(ProfileServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<AlternativeResponse> handleProfileServiceException(ProfileServiceException exception) {
        AlternativeResponse alternativeResponse = AlternativeResponse.instance(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), exception.getCause().getMessage());
        return ResponseEntity.badRequest().body(alternativeResponse);
    }
}
