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
    ADMIN_CREATE_WORKER("admin:create_worker")
    ;
    private final String permission;
}
