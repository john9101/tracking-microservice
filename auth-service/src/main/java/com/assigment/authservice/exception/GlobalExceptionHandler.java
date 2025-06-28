package com.assigment.authservice.exception;

import com.assigment.authservice.dto.response.ErrorResponse;
import com.assigment.authservice.util.ConstantUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

//    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
//    public ResponseEntity<ErrorResponse> handleAuthError(Exception exception) {
//
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request, Locale locale) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            String message = messageSource.getMessage(error, locale);
            errors.put(error.getField(), message);
        });

        String message = messageSource.getMessage(ConstantUtil.VALIDATION_INVALID, null, locale);
        ErrorResponse errorResponse = ErrorResponse.instance(HttpStatus.BAD_REQUEST.value(), message, errors);
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
