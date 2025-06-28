package com.tracking.commonservice.event;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RegistrationEvent {
    private String email;
    private String name;
}
