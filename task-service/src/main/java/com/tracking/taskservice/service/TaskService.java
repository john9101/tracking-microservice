package com.tracking.taskservice.service;

import com.tracking.taskservice.dto.request.CreateTaskRequest;
import com.tracking.taskservice.dto.request.UpdateTaskRequest;
import com.tracking.taskservice.dto.response.CreatedTaskResponse;
import com.tracking.taskservice.dto.response.GotTaskResponse;
import com.tracking.taskservice.dto.response.ListTasksResponse;
import com.tracking.taskservice.dto.response.UpdatedTaskResponse;
import com.tracking.taskservice.exception.TaskServiceException;

import java.util.Locale;

public interface TaskService {
    CreatedTaskResponse createTask(CreateTaskRequest createTask, String username, String externalConstraintMessage, String failMessage);

    GotTaskResponse getTask(Long id, String username, Locale locale);

    UpdatedTaskResponse updateTask(Long id, String username, UpdateTaskRequest request, Locale locale);

    void deleteTask(Long id, String username, Locale locale);

    ListTasksResponse listTasks(String username, Locale locale);
}
