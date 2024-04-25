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
    public static final String ERROR_TASK_NOT_ASSIGNED = "Task is not assigned";
    public static final String ERROR_INVALID_DEADLINE_PROVIDED = "Task deadline is not provided appropriately";
    public static final String ERROR_TASK_ASSIGNED = "Task already assigned";
    public static final String ERROR_TASK_CLOSED = "Task already closed";

    public static final String ERROR_WORKER_NOT_FOUND_BY_ID = "Worker not found by given id";
    public static final String ERROR_WORKER_NOT_FOUND_BY_USERNAME = "Worker not found by given username";
    public static final String ERROR_USERNAME_EXISTS = "Username already exists";
    public static final String ERROR_INVALID_USERNAME_PASSWORD = "Invalid username or password";
    public static final String ERROR_EMAIL_ALREADY_EXISTS = "Email already exists";
    public static final String ERROR_INVALID_CREDENTIALS = "Invalid credentials provided";

    public static final String ERROR_INTERNAL_SERVER_ERROR = "Internal server error.";
    public static final String ERROR_INVALID_OPERATION = "Invalid operation!";
}
