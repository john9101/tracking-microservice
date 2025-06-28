package com.tracking.taskservice.service.impl;

import com.tracking.commonservice.dto.UserDtoRpc;
import com.tracking.commonservice.service.UserServiceRpc;
import com.tracking.taskservice.dto.request.CreateTaskRequest;
import com.tracking.taskservice.dto.request.UpdateTaskRequest;
import com.tracking.taskservice.dto.response.CreatedTaskResponse;
import com.tracking.taskservice.dto.response.GotTaskResponse;
import com.tracking.taskservice.dto.response.ListTasksResponse;
import com.tracking.taskservice.dto.response.UpdatedTaskResponse;
import com.tracking.taskservice.entity.Priority;
import com.tracking.taskservice.entity.Status;
import com.tracking.taskservice.entity.Task;
import com.tracking.taskservice.exception.TaskServiceException;
import com.tracking.taskservice.helper.TemporalHelper;
import com.tracking.taskservice.repository.TaskRepository;
import com.tracking.taskservice.service.TaskService;
import com.tracking.taskservice.util.ConstantUtil;
import com.tracking.taskservice.workflow.TaskWorkflow;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    @DubboReference
    private UserServiceRpc userServiceRpc;
    private final TaskRepository taskRepository;
    private final WorkflowClient workflowClient;
    private final MessageSource messageSource;
    private final MeterRegistry meterRegistry;
    private Timer timer;


    @Override
    public CreatedTaskResponse createTask(
            CreateTaskRequest createTaskRequest,
            String username,
            String externalConstraintMessage,
            String failMessage
    ) {
        try {
            UserDtoRpc userDtoRpc = userServiceRpc.getUser(username);
            if (userDtoRpc != null) {
                TaskWorkflow taskWorkflow = workflowClient.newWorkflowStub(TaskWorkflow.class,
                        WorkflowOptions.newBuilder().setTaskQueue(TemporalHelper.TASK_QUEUE).build()
                );
                Long taskId = taskWorkflow.createTask(createTaskRequest, userDtoRpc.getId());
                return CreatedTaskResponse.builder().id(taskId).build();
            } else {
                throw new TaskServiceException(externalConstraintMessage);
            }
        } catch (Exception e) {
            throw new TaskServiceException(failMessage, e);
        }
    }

    @Override
    @Cacheable(value = "tasks", key = "#id")
    public GotTaskResponse getTask(Long id, String username, Locale locale) {
        timer = Timer.builder("task.get.timer").description("A get task timer").register(meterRegistry);
        return timer.record(() -> {
            Task task = manifestLifeTask(id, username, locale);
            return GotTaskResponse.builder()
                    .id(task.getId())
                    .title(task.getTitle())
                    .description(task.getDescription())
                    .status(task.getStatus())
                    .priority(task.getPriority())
                    .dueTime(task.getDueTime().toString())
                    .build();
        });
    }

    @Override
    @CircuitBreaker(name = "taskServiceCB", fallbackMethod = "taskServiceFallBack")
    @Retry(name = "taskServiceRetry")
    @CachePut(value = "tasks", key = "#id")
    public UpdatedTaskResponse updateTask(Long id, String username, UpdateTaskRequest request, Locale locale) {
        Task task = manifestLifeTask(id, username, locale);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(Priority.valueOf(request.getPriority()));
        task.setStatus(Status.valueOf(request.getStatus()));
        task.setDueTime(request.getDueTime());
        try {
            task = taskRepository.save(task);
            return UpdatedTaskResponse.builder().id(task.getId()).build();
        } catch (Exception e) {
            String failMessage = messageSource.getMessage(ConstantUtil.REQUEST_UPDATE_FAIL, null, locale);
            throw new TaskServiceException(failMessage, e);
        }
    }

    @Override
    @CacheEvict(value = "tasks", key = "#id")
    public void deleteTask(Long id, String username, Locale locale) {
        try {
            Task task = manifestLifeTask(id, username, locale);
            task.setDeleted(true);
            taskRepository.save(task);
        } catch (Exception e) {
            String failMessage = messageSource.getMessage(ConstantUtil.REQUEST_DELETE_FAIL, null, locale);
            throw new TaskServiceException(failMessage, e);
        }
    }

    @Override
    public ListTasksResponse listTasks(String username, Locale locale) {
        UserDtoRpc userDtoRpc = userServiceRpc.getUser(username);
        if (userDtoRpc == null) throw new TaskServiceException(externalConstraintMessage(username, locale));
        List<Task> tasks = taskRepository.findByUserIdAndDeleted(userDtoRpc.getId(), false);
        List<ListTasksResponse.Item> items = tasks.stream().map(task ->
                ListTasksResponse.Item.builder()
                        .id(task.getId())
                        .title(task.getTitle())
                        .status(task.getStatus())
                        .priority(task.getPriority())
                        .dueTime(task.getDueTime().toString())
                        .build()
        ).toList();
        return ListTasksResponse.builder().items(items).build();
    }

    private Task manifestLifeTask(Long id, String username, Locale locale) {
        UserDtoRpc userDtoRpc = userServiceRpc.getUser(username);
        if (userDtoRpc == null) throw new TaskServiceException(externalConstraintMessage(username, locale));
        return taskRepository.findByIdAndUserIdAndDeleted(id, userDtoRpc.getId(), false)
                .orElseThrow(() -> new EntityNotFoundException(internalConstraintMessage(id, locale)));
    }

    private String externalConstraintMessage(String username, Locale locale) {
        return messageSource.getMessage(ConstantUtil.EXTERNAL_CONSTRAINT_USERNAME_NOT_FOUND, new Object[]{username}, locale);
    }

    private String internalConstraintMessage(Long id, Locale locale) {
        return messageSource.getMessage(ConstantUtil.INTERNAL_CONSTRAINT_ID_NOT_FOUND, new Object[]{id}, locale);
    }

    public UpdatedTaskResponse taskServiceFallBack(Long id, String username, UpdateTaskRequest request, Locale locale, Throwable throwable) {
        Status status;
        Priority priority;
        if (!Arrays.stream(Status.values()).map(Status::name).toList().contains(request.getStatus())) {
            status = Arrays.stream(Status.values()).findAny().orElse(Status.IN_PROGRESS);
        }else {
            status = Status.valueOf(request.getStatus());
        }

        if (!Arrays.stream(Priority.values()).map(Priority::name).toList().contains(request.getPriority())){
            priority = Arrays.stream(Priority.values()).findAny().orElse(Priority.LOW);
        }else {
            priority = Priority.valueOf(request.getPriority());
        }

        Task task = manifestLifeTask(id, username, locale);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(priority);
        task.setStatus(status);
        task = taskRepository.save(task);
        return UpdatedTaskResponse.builder().id(task.getId()).build();
    }
}
