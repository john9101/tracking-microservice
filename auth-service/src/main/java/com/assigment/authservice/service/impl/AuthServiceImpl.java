package com.assigment.authservice.service.impl;

import com.assigment.authservice.dto.request.LoginRequest;
import com.assigment.authservice.dto.request.RegisterRequest;
import com.assigment.authservice.dto.response.LoggedInResponse;
import com.assigment.authservice.dto.response.RegisteredResponse;
import com.assigment.authservice.service.AuthService;
import com.assigment.authservice.service.TokenService;
import com.assigment.authservice.util.ConstantUtil;
import com.nimbusds.jose.JOSEException;
import com.tracking.commonservice.dto.UserDtoRpc;
import com.tracking.commonservice.event.RegistrationEvent;
import com.tracking.commonservice.service.UserServiceRpc;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @DubboReference
    private UserServiceRpc userServiceRpc;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;

    private final StreamBridge streamBridge;

    private final MeterRegistry meterRegistry;

    private Counter counter;

    private Timer timer;

    @Override
    public RegisteredResponse register(RegisterRequest registerRequest) {
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        UserDtoRpc userDtoRpc = userServiceRpc.createUser(
                Map.of(
                        "name", registerRequest.getName(),
                        "email", registerRequest.getEmail(),
                        "username", registerRequest.getUsername(),
                        "encodedPassword", encodedPassword
                ),
                Collections.singletonList(ConstantUtil.ROLE_USER)
        );

        RegistrationEvent registrationEvent = RegistrationEvent.builder()
                .email(userDtoRpc.getEmail())
                .name(userDtoRpc.getName())
                .build();

        Message<RegistrationEvent> registrationEventMessage = MessageBuilder.withPayload(registrationEvent).build();
        streamBridge.send("registration-out-0", registrationEventMessage);

        return RegisteredResponse.builder()
                .id(userDtoRpc.getId())
                .name(userDtoRpc.getName())
                .email(userDtoRpc.getEmail())
                .username(userDtoRpc.getUsername())
                .build();
    }

    @Override
    public LoggedInResponse login(LoginRequest loginRequest) {
        counter = Counter.builder("auth.login.counter").description("A auth login counter").register(meterRegistry);
        timer = Timer.builder("auth.login.timer").description("A auth login timer").register(meterRegistry);
        return timer.record(() -> {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String accessToken = tokenService.generateToken(userDetails);
            counter.increment();
            return LoggedInResponse.builder().accessToken(accessToken).build();
        });
    }
}

