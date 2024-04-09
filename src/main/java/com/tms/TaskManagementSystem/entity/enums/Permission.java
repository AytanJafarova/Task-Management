package com.tms.TaskManagementSystem.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Permission {

    ADMIN_SAVE_ORGANIZATION("admin:save_organization"),
    ADMIN_UPDATE_ORGANIZATION("admin:update_organization"),
    ADMIN_DELETE_ORGANIZATION("admin:delete_organization"),
    ADMIN_INACTIVATE_ORGANIZATION("admin:inactivate_organization"),
    ADMIN_GET_ALL_ORGANIZATIONS("admin:all_organizations"),


    ADMIN_DELETE_TASK("admin:delete:task"),

    ADMIN_GET_ALL_WORKERS("admin:all_workers"),
    ADMIN_INACTIVATE_WORKER("admin:inactivate_worker"),
    ADMIN_DELETE_WORKER("admin:delete_worker"),



    USER_GET_ORGANIZATION("user:organization_by_id"),
    USER_GET_ACTIVE_ORGANIZATIONS("user:active_organizations"),
    USER_GET_ORGANIZATION_WORKERS("user:organization_workers"),


    USER_GET_ACTIVE_WORKERS("user:active_workers"),
    USER_GET_WORKER("user:worker"),
    USER_WORKER_BY_ORGANIZATION("user:worker_by_organization"),
    USER_UPDATE_WORKER("user:update_worker"),

    USER_ASSIGN_TASK("user:assign_task"),
    USER_UPDATE_TASK("user:update_task"),
    USER_CLOSE_TASK("user:close_task"),
    USER_ARRIVED_TASK("user:arrived_task"),
    USER_TASKS_BY_WORKER("user:tasks_by_worker"),
    USER_TASKS_TODO("user:tasks_todo"),
    USER_TASKS_INPROGRESS("user:tasks_inprogress"),
    USER_TASKS_CLOSED("user:tasks_closed"),
    USER_GET_TASKS("user:tasks"),
    USER_GET_TASK("user:task"),
    USER_SAVE_TASK("user:save_task")
    ;
    private final String permission;
}
