package com.tracking.profileservice.exception;

public class ProfileServiceException extends RuntimeException {
    public ProfileServiceException(String message) {
        super(message);
    }

    public ProfileServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
