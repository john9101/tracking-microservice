package com.tracking.taskservice.workflow;

import com.tracking.taskservice.dto.request.CreateTaskRequest;
import com.tracking.taskservice.exception.TaskServiceException;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface TaskWorkflow {

    @WorkflowMethod
    Long createTask(CreateTaskRequest createTaskRequest, Long userId);
}

