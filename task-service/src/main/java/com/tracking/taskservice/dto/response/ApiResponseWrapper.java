package com.tracking.taskservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter @Setter
public class ApiResponseWrapper<T> {
    private int status;
    private String message;
    private T data;

    public ApiResponseWrapper(String message, T data, int status) {
        this.message = message;
        this.data = data;
        this.status = status;
    }

    public static <T> ApiResponseWrapper<T> instance(String message, T data, int status) {
        return new ApiResponseWrapper<>(message, data, status);
    }
}
