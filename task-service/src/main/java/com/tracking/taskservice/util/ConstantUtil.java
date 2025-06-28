package com.tracking.taskservice.util;

import org.apache.dubbo.common.logger.FluentLogger;

import java.io.Serializable;

public class ConstantUtil implements Serializable {
    public static final String VALIDATION_INVALID = "message.validation.invalid";
    public static final String FIELD_TITLE_NOT_BLANK= "{message.field.title.notBlank}";
    public static final String FIELD_PRIORITY_NOT_NULL= "{message.field.priority.notNull}";
    public static final String FIELD_DUE_TIME_NOT_NULL= "{message.field.dueTime.notNull}";
    public static final String REQUEST_CREATE_SUCCESS= "message.request.create.success";
    public static final String REQUEST_CREATE_FAIL= "message.request.create.fail";
    public static final String REQUEST_UPDATE_SUCCESS= "message.request.update.success";
    public static final String REQUEST_UPDATE_FAIL= "message.request.update.fail";
    public static final String REQUEST_DELETE_SUCCESS= "message.request.delete.success";
    public static final String REQUEST_DELETE_FAIL= "message.request.delete.fail";
    public static final String REQUEST_GET_SUCCESS= "message.request.get.success";
    public static final String REQUEST_LIST_SUCCESS= "message.request.list.success";
    public static final String INTERNAL_CONSTRAINT_ID_NOT_FOUND = "message.internal.constraint.id.notFound";
    public static final String EXTERNAL_CONSTRAINT_USERNAME_NOT_FOUND= "message.external.constraint.username.notFound";
}