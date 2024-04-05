package com.tms.TaskManagementSystem.response.Worker;

import com.tms.TaskManagementSystem.response.Organization.OrganizationResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkerForTaskResponse {
    private String username;
    private String password;
}