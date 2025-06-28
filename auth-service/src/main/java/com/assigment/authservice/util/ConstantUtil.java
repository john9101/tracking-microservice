package com.assigment.authservice.util;

import java.io.Serializable;

public class ConstantUtil implements Serializable {
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String VALIDATION_INVALID = "message.validation.invalid";
    public static final String FIELD_PASSWORD_NOT_BLANK = "{message.field.password.notBlank}";
    public static final String FIELD_PASSWORD_SIZE = "{message.field.password.size}";
    public static final String FIELD_EMAIL_NOT_BLANK = "{message.field.email.notBlank}";
    public static final String FIELD_EMAIL_INVALID_FORMAT = "{message.field.email.invalidFormat}";
    public static final String FIELD_NAME_NOT_BLANK = "{message.field.name.notBlank}";
    public static final String FIELD_USERNAME_NOT_BLANK = "{message.field.username.notBlank}";
    public static final String CONSTRAINT_USERNAME_NOT_FOUND = "message.constraint.username.notFound";
    public static final String CONSTRAINT_USERNAME_EXISTED = "message.constraint.username.existed";
    public static final String CONTRAINT_PASSWORD_INCORRECT = "message.constraint.password.incorrect";
//    public static final String CONSTRAINT_EMAIL_EXISTED = "message.constraint.email.existed";
//    public static final String CONSTRAINT_EMAIL_NOT_FOUND = "message.constraint.email.notFound";
    public static final String REQUEST_REGISTER_SUCCESS= "message.request.register.success";
    public static final String REQUEST_LOGIN_SUCCESS= "message.request.login.success";
}
