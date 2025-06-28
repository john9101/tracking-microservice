package com.tracking.taskservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter @Setter
public class AlternativeResponse {
    private int status;
    private String message;
    private String error;
    private Map<String, String> errors;

    public AlternativeResponse(int status, String message, String error, Map<String, String> errors) {
        this.status = status;
        this.message = message;
        this.error = error;
        this.errors = errors;
    }

    public static AlternativeResponse instance(int status, String message){
        return new AlternativeResponse(status, message, null, null);
    }

    public static AlternativeResponse instance(int status, String message, String error){
        return new AlternativeResponse(status, message, error, null);
    }

    public static AlternativeResponse instance(int status, String message, Map<String, String> errors){
        return new AlternativeResponse(status, message, null, errors);
    }
}
