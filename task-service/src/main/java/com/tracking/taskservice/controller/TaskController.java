package com.tracking.taskservice.controller;

import com.tracking.taskservice.dto.request.CreateTaskRequest;
import com.tracking.taskservice.dto.request.UpdateTaskRequest;
import com.tracking.taskservice.dto.response.*;
import com.tracking.taskservice.exception.TaskServiceException;
import com.tracking.taskservice.service.TaskService;
import com.tracking.taskservice.util.ConstantUtil;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@CrossOrigin("*")
@RestController
@RequestMapping("/tasks")
@Tag(name = "Task API")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final MessageSource messageSource;


    @PostMapping
    @Operation(summary = "Create task")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Success request",
                    content = @Content(schema = @Schema(implementation = ApiResponseWrapper.class),
                            examples = @ExampleObject(value = """
                                        {
                                          "status": 201,
                                          "message": "Create task request successful",
                                          "data": {
                                              "id": 1
                                          }
                                        }
                                    """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = AlternativeResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Invalid",
                                            value = """
                                                        {
                                                          "status": 400,
                                                          "message": "Some input fields are invalid",
                                                          "errors": {
                                                              "title": "Title cannot be blank"
                                                          }
                                                        }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Fail",
                                            value = """
                                                        {
                                                          "status": 400,
                                                          "message": "Create task request failed"
                                                        }
                                                    """
                                    )
                            }
                    )
            )
    })
    public ResponseEntity<ApiResponseWrapper<CreatedTaskResponse>> createTask(@Valid @RequestBody CreateTaskRequest createTaskRequest, HttpServletRequest servletRequest, Locale locale) {
        String username = getPrincipal(servletRequest);
        String externalConstraintMessage = externalConstraintMessage(username, locale);
        String failMessage = messageSource.getMessage(ConstantUtil.REQUEST_CREATE_FAIL, null, locale);
        String successMessage = messageSource.getMessage(ConstantUtil.REQUEST_CREATE_SUCCESS, null, locale);
        CreatedTaskResponse createdTaskResponse = taskService.createTask(createTaskRequest, username, externalConstraintMessage, failMessage);
        ApiResponseWrapper<CreatedTaskResponse> apiResponseWrapper = ApiResponseWrapper.instance(successMessage, createdTaskResponse, HttpStatus.CREATED.value());
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponseWrapper);
    }


    @GetMapping("/list")
    public ResponseEntity<ApiResponseWrapper<ListTasksResponse>> listTasks(
            HttpServletRequest servletRequest,
            Locale locale
    ) {
        String username = getPrincipal(servletRequest);
        String successMessage = messageSource.getMessage(ConstantUtil.REQUEST_LIST_SUCCESS, null, locale);
        ListTasksResponse response = taskService.listTasks(username, locale);
        ApiResponseWrapper<ListTasksResponse> responseWrapper = ApiResponseWrapper.instance(
                successMessage,
                response,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(responseWrapper);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<GotTaskResponse>> getTask(
            @PathVariable Long id,
            HttpServletRequest servletRequest,
            Locale locale
    ) {
        String username = getPrincipal(servletRequest);
        String successMessage = messageSource.getMessage(ConstantUtil.REQUEST_GET_SUCCESS, null, locale);
        GotTaskResponse response = taskService.getTask(id, username, locale);
        ApiResponseWrapper<GotTaskResponse> responseWrapper = ApiResponseWrapper.instance(
                successMessage,
                response,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(responseWrapper);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<UpdatedTaskResponse>> updateTask(
            @PathVariable Long id,
            @RequestBody UpdateTaskRequest request,
            HttpServletRequest servletRequest,
            Locale locale
    ) {
        String username = getPrincipal(servletRequest);
        String successMessage = messageSource.getMessage(ConstantUtil.REQUEST_UPDATE_SUCCESS, null, locale);
        UpdatedTaskResponse response = taskService.updateTask(id, username, request, locale);
        ApiResponseWrapper<UpdatedTaskResponse> responseWrapper = ApiResponseWrapper.instance(
                successMessage,
                response,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(responseWrapper);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<Void>> deleteTask(
            @PathVariable Long id,
            HttpServletRequest servletRequest,
            Locale locale
    ) {
        String username = getPrincipal(servletRequest);
        String successMessage = messageSource.getMessage(ConstantUtil.REQUEST_DELETE_SUCCESS, null, locale);
        taskService.deleteTask(id, username, locale);
        ApiResponseWrapper<Void> responseWrapper = ApiResponseWrapper.instance(
                successMessage,
                null,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(responseWrapper);
    }

    private String externalConstraintMessage(String username, Locale locale) {
        return messageSource.getMessage(ConstantUtil.EXTERNAL_CONSTRAINT_USERNAME_NOT_FOUND, new Object[]{username}, locale);
    }

    private String getPrincipal(HttpServletRequest servletRequest) {
        return servletRequest.getHeader("X-Username");
    }
}
