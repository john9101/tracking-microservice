package com.tracking.taskservice.activity;

import com.tracking.taskservice.dto.request.CreateTaskRequest;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface TaskActivity {
    @ActivityMethod
    Long saveTask(CreateTaskRequest createTaskRequest, Long userId);
}

