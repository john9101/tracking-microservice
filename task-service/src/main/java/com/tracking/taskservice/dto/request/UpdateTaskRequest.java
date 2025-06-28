package com.tracking.taskservice.dto.request;

import com.tracking.taskservice.entity.Priority;
import com.tracking.taskservice.entity.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class UpdateTaskRequest {
    private String title;
    private String description;
//    private Status status;
    private String status;
//    private Priority priority;
    private String priority;
    private LocalDateTime dueTime;
}
