package com.tracking.profileservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
public class GotProfileResponse {
    private Long id;
    private String username;
    private String name;
    private String email;
}
