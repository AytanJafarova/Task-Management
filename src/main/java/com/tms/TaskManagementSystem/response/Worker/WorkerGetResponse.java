package com.tms.TaskManagementSystem.response.Worker;

import com.tms.TaskManagementSystem.response.Organization.OrganizationForWorkerResponse;
import com.tms.TaskManagementSystem.response.Organization.OrganizationResponse;
import com.tms.TaskManagementSystem.response.Task.TaskForWorkerResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkerGetResponse {
    private String username;
    private String password;
    private String email;
    private OrganizationForWorkerResponse organization;
    private List<TaskForWorkerResponse> tasks;
}
