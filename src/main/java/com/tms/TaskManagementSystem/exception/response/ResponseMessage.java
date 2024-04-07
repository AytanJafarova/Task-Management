package com.tms.TaskManagementSystem.exception.response;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseMessage {
    public static final String ERROR_ORGANIZATION_EXISTS = "Organization with the specified name already exists";
    public static final String ERROR_INVALID_ORGANIZATION_NAME = "Invalid organization name";
    public static final String ERROR_ORGANIZATION_NOT_FOUND_BY_ID = "Organization not found by given id";
    public static final String ERROR_TASK_ALREADY_EXISTS = "Task with the specified header already exists";
    public static final String ERROR_INVALID_TASK_HEADER = "Invalid task header";
    public static final String ERROR_INVALID_PRIORITY_PROVIDED = "Invalid priority value provided";
    public static final String ERROR_TASK_NOT_FOUND_BY_ID = "Task not found by given id";
    public static final String ERROR_TASK_NOT_ASSIGNED = "Task is not assigned to any worker";
    public static final String ERROR_DEADLINE_NOT_SPECIFIED = "Task deadline is not specified";
    public static final String ERROR_TASK_ASSIGNED = "Task already assigned";
    public static final String ERROR_WORKER_NOT_FOUND_BY_ID = "Worker not found by given id";
    public static final String ERROR_USERNAME_EXISTS = "Username already exists";
    public static final String ERROR_INVALID_USERNAME_PASSWORD = "Invalid username or password";
    public static final String ERROR_INTERNAL_SERVER_ERROR = "Internal server error.";
}
