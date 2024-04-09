package com.tms.TaskManagementSystem.request.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    private String username;
    private String name;
    private String surname;
    private String password;
    private String email;
    private Long organization_id;
}
