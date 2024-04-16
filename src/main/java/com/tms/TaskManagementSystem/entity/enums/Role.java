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
                    Permission.ADMIN_GET_ALL_WORKERS
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
