package com.tracking.userservice.audit;

import com.tracking.userservice.util.RequestContextHolderUtil;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(RequestContextHolderUtil.getUsername());
    }
}

