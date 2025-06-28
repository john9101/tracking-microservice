package com.tracking.taskservice.workflow.impl;

import com.tracking.taskservice.activity.TaskActivity;
import com.tracking.taskservice.dto.request.CreateTaskRequest;
import com.tracking.taskservice.helper.TemporalHelper;
import com.tracking.taskservice.workflow.TaskWorkflow;
import io.temporal.workflow.Workflow;


public class TaskWorkflowImpl implements TaskWorkflow {
    private final TaskActivity taskActivity = Workflow.newActivityStub(TaskActivity.class, TemporalHelper.activityOptions());

    @Override
    public Long createTask(CreateTaskRequest createTaskRequest, Long userId) {
        return taskActivity.saveTask(createTaskRequest, userId);
    }
}

