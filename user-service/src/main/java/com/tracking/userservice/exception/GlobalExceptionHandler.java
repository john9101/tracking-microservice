package com.tracking.userservice.exception;

import com.tracking.userservice.dto.response.AlternativeResponse;
import com.tracking.userservice.util.ConstantUtil;
import jakarta.persistence.EntityNotFoundException;
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

    @ExceptionHandler(UserServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<AlternativeResponse> handleProfileServiceException(UserServiceException exception) {
        AlternativeResponse alternativeResponse = AlternativeResponse.instance(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), exception.getCause().getMessage());
        return ResponseEntity.badRequest().body(alternativeResponse);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<AlternativeResponse> handleEntityNotFoundException(EntityNotFoundException exception) {
        AlternativeResponse alternativeResponse = AlternativeResponse.instance(HttpStatus.NOT_FOUND.value(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(alternativeResponse);
    }
}
