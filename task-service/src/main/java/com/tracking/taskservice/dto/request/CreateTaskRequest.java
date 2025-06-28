package com.tracking.taskservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tracking.taskservice.annotation.EnumConstraint;
import com.tracking.taskservice.entity.Priority;
import com.tracking.taskservice.entity.Status;
import com.tracking.taskservice.util.ConstantUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@Schema(description = "Task information")
public class CreateTaskRequest {
    @NotBlank(message = ConstantUtil.FIELD_TITLE_NOT_BLANK)
    @Schema(description = "Task title", example = "Finish project report")
    private String title;

    @Schema(description = "Task description", example = "Complete the final report and submit it by due date")
    private String description;

    @NotNull(message = ConstantUtil.FIELD_PRIORITY_NOT_NULL)
    @Schema(description = "Task priority", example = "HIGH")
    private Priority priority;

    @NotNull(message = ConstantUtil.FIELD_DUE_TIME_NOT_NULL)
    @Schema(description = "Task due time", example = "01-05-2025 17:00")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime dueTime;
}
