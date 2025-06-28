package com.assigment.authservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter @Setter
public class ErrorResponse {
    private int status;
    private String message;
    private String error;
    private Map<String, String> errors;

    public ErrorResponse(int status, String message, String error) {
        this.status = status;
        this.message = message;
        this.error = error;
    }

    public ErrorResponse(int status, String message, Map<String, String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public static ErrorResponse instance(int status, String message, String error){
        return new ErrorResponse(status, message, error);
    }

    public static ErrorResponse instance(int status, String message, Map<String, String> errors){
        return new ErrorResponse(status, message, errors);
    }
}
