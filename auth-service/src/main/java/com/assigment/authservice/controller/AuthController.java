package com.assigment.authservice.controller;

import com.assigment.authservice.dto.request.LoginRequest;
import com.assigment.authservice.dto.request.RegisterRequest;
import com.assigment.authservice.dto.response.ApiResponseWrapper;
import com.assigment.authservice.dto.response.LoggedInResponse;
import com.assigment.authservice.dto.response.RegisteredResponse;
import com.assigment.authservice.service.AuthService;
import com.assigment.authservice.service.impl.AuthServiceImpl;
import com.assigment.authservice.util.ConstantUtil;
import com.nimbusds.jose.JOSEException;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.concurrent.ExecutionException;

@CrossOrigin("*")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth REST API")
public class AuthController {
    private final AuthService authService;
    private final Tracer tracer;
    private final MessageSource messageSource;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseWrapper<RegisteredResponse>> register(@Valid @RequestBody RegisterRequest request, Locale locale) {
        RegisteredResponse registeredResponse = authService.register(request);
        String message = messageSource.getMessage(ConstantUtil.REQUEST_REGISTER_SUCCESS, null, locale);
        ApiResponseWrapper<RegisteredResponse> apiResponseWrapper = ApiResponseWrapper.instance(
                message,
                registeredResponse,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(apiResponseWrapper);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseWrapper<LoggedInResponse>> login(@Valid @RequestBody LoginRequest request, Locale locale) throws Exception {
        Span span = tracer.spanBuilder("login-request").startSpan();
        try (Scope scope = span.makeCurrent()) {
            LoggedInResponse loggedInResponse = authService.login(request);
            span.setStatus(io.opentelemetry.api.trace.StatusCode.OK);
            scope.close();
            String message = messageSource.getMessage(ConstantUtil.REQUEST_LOGIN_SUCCESS, null, locale);
            ApiResponseWrapper<LoggedInResponse> apiResponseWrapper = ApiResponseWrapper.instance(
                    message,
                    loggedInResponse,
                    HttpStatus.OK.value()
            );
            return ResponseEntity.ok(apiResponseWrapper);
        } finally {
            span.end();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok().build();
    }
}
