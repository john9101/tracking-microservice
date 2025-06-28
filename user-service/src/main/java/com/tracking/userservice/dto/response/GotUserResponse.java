package com.tracking.userservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
public class GotUserResponse {
    private Long id;
    private String name;
    private String email;
    private String username;
}
