package com.tms.TaskManagementSystem.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER(
        Set.of(
                Permission.USER_ARRIVED_TASK,
                Permission.USER_ASSIGN_TASK,
                Permission.USER_CLOSE_TASK,
                Permission.USER_GET_TASK,
                Permission.USER_GET_ORGANIZATION,
                Permission.USER_GET_ACTIVE_ORGANIZATIONS,
                Permission.USER_GET_ACTIVE_WORKERS,
                Permission.USER_GET_ORGANIZATION_WORKERS,
                Permission.USER_SAVE_TASK,
                Permission.USER_TASKS_BY_WORKER,
                Permission.USER_TASKS_TODO,
                Permission.USER_TASKS_INPROGRESS,
                Permission.USER_TASKS_CLOSED,
                Permission.USER_UPDATE_TASK,
                Permission.USER_GET_TASKS,
                Permission.USER_GET_WORKER,
                Permission.USER_UPDATE_WORKER,
                Permission.USER_WORKER_BY_ORGANIZATION
                )
    ),
    ADMIN(
            Set.of(
                    Permission.ADMIN_SAVE_ORGANIZATION,
                    Permission.ADMIN_DELETE_ORGANIZATION,
                    Permission.ADMIN_DELETE_TASK,
                    Permission.ADMIN_DELETE_WORKER,
                    Permission.ADMIN_GET_ALL_ORGANIZATIONS,
                    Permission.ADMIN_UPDATE_ORGANIZATION,
                    Permission.ADMIN_INACTIVATE_ORGANIZATION,
                    Permission.ADMIN_INACTIVATE_WORKER,
                    Permission.ADMIN_GET_ALL_WORKERS,


                    Permission.USER_ARRIVED_TASK,
                    Permission.USER_ASSIGN_TASK,
                    Permission.USER_CLOSE_TASK,
                    Permission.USER_GET_TASK,
                    Permission.USER_GET_ORGANIZATION,
                    Permission.USER_GET_ACTIVE_ORGANIZATIONS,
                    Permission.USER_GET_ACTIVE_WORKERS,
                    Permission.USER_GET_ORGANIZATION_WORKERS,
                    Permission.USER_SAVE_TASK,
                    Permission.USER_TASKS_BY_WORKER,
                    Permission.USER_TASKS_TODO,
                    Permission.USER_TASKS_INPROGRESS,
                    Permission.USER_TASKS_CLOSED,
                    Permission.USER_UPDATE_TASK,
                    Permission.USER_GET_TASKS,
                    Permission.USER_GET_WORKER,
                    Permission.USER_UPDATE_WORKER,
                    Permission.USER_WORKER_BY_ORGANIZATION
            )
    );
    private final Set<Permission> permissions;
    public List<SimpleGrantedAuthority> getAuthorities()
    {
       var authorities =  getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
       authorities.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
       return authorities;
    }
}
