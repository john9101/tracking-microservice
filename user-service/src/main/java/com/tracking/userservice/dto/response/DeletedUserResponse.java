package com.tracking.userservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
public class DeletedUserResponse {
    private Long id;
}
