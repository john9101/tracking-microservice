package com.tracking.profileservice.service.impl;

import com.tracking.commonservice.dto.UserDtoRpc;
import com.tracking.commonservice.service.UserServiceRpc;
import com.tracking.profileservice.dto.request.UpdateProfileRequest;
import com.tracking.profileservice.dto.response.GotProfileResponse;
import com.tracking.profileservice.dto.response.UpdatedProfileResponse;
import com.tracking.profileservice.exception.ProfileServiceException;
import com.tracking.profileservice.service.ProfileService;
import com.tracking.profileservice.util.ConstantUtil;
import io.micrometer.core.instrument.MeterRegistry;
import io.prometheus.metrics.core.metrics.Gauge;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    @DubboReference
    private UserServiceRpc userServiceRpc;

    private final MessageSource messageSource;

    private final MeterRegistry meterRegistry;

    private AtomicInteger gauge;

    @Override
    public GotProfileResponse getProfile(String username, Locale locale) {
        UserDtoRpc userDtoRpc = userServiceRpc.getUser(username);
        if (userDtoRpc == null) throw new ProfileServiceException(externalConstraintMessage(username, locale));
        return GotProfileResponse.builder()
                .id(userDtoRpc.getId())
                .username(userDtoRpc.getUsername())
                .email(userDtoRpc.getEmail())
                .name(userDtoRpc.getName())
                .build();
    }
    @Override
    public UpdatedProfileResponse updateProfile(String username, Locale locale, UpdateProfileRequest request) {
        try {
            gauge = meterRegistry.gauge("profile.update.gauge",new AtomicInteger(0));
            Objects.requireNonNull(gauge).incrementAndGet();
            UserDtoRpc userDtoRpc = userServiceRpc.updateUser(request.getName(), request.getEmail(), username);
            if (userDtoRpc == null) throw new ProfileServiceException(externalConstraintMessage(username, locale));
            Thread.sleep(200);
            return UpdatedProfileResponse.builder().id(userDtoRpc.getId()).build();
        }catch (Exception e){
            String failMessage = messageSource.getMessage(ConstantUtil.REQUEST_UPDATE_FAIL, null, locale);
            throw new ProfileServiceException(failMessage, e);
        }finally {
            Objects.requireNonNull(gauge).decrementAndGet();
        }
    }

    private String externalConstraintMessage(String username, Locale locale) {
        return messageSource.getMessage(ConstantUtil.EXTERNAL_CONSTRAINT_USERNAME_NOT_FOUND, new Object[]{username}, locale);
    }
}
