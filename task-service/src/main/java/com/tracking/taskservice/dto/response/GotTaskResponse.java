package com.tracking.taskservice.dto.response;

import com.tracking.taskservice.entity.Priority;
import com.tracking.taskservice.entity.Status;
import lombok.*;


@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class GotTaskResponse {
    private Long id;
    private String title;
    private String description;
    private Priority priority;
    private Status status;
    private String dueTime;
}
