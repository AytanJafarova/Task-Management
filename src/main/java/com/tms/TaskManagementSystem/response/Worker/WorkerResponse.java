package com.tms.TaskManagementSystem.response.Worker;

import com.tms.TaskManagementSystem.response.Organization.OrganizationForWorkerResponse;
import com.tms.TaskManagementSystem.response.Organization.OrganizationResponse;
import com.tms.TaskManagementSystem.response.Task.TaskForWorkerResponse;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkerResponse {
    private String username;
    private String password;
    private String email;
    private OrganizationForWorkerResponse organization;
    private List<TaskForWorkerResponse> tasks;
    private boolean isDeleted;
}