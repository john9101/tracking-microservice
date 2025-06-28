package com.tracking.taskservice.activity.impl;

import com.tracking.taskservice.activity.TaskActivity;
import com.tracking.taskservice.dto.request.CreateTaskRequest;
import com.tracking.taskservice.entity.Task;
import com.tracking.taskservice.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskActivityImpl implements TaskActivity {
    private final TaskRepository taskRepository;

    @Override
    public Long saveTask(CreateTaskRequest createTaskRequest, Long userId)  {
        Task task = new Task();
        task.setTitle(createTaskRequest.getTitle());
        task.setDescription(createTaskRequest.getDescription());
        task.setPriority(createTaskRequest.getPriority());
        task.setDueTime(createTaskRequest.getDueTime());
        task.setUserId(userId);
        task = taskRepository.save(task);
        return task.getId();
    }
}

