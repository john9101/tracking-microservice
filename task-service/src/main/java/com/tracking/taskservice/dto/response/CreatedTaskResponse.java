package com.tracking.taskservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class CreatedTaskResponse {
    private Long id;
}
