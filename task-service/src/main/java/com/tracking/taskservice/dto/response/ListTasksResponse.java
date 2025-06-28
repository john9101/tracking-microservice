package com.tracking.taskservice.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.tracking.taskservice.entity.Priority;
import com.tracking.taskservice.entity.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@Builder
public class ListTasksResponse {
    private List<Item> items;

    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Getter @Setter
    public static class Item{
        private Long id;
        private String title;
        private Priority priority;
        private Status status;
        private String dueTime;
    }
}
