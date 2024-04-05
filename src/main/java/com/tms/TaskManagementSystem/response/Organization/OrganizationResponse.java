package com.tms.TaskManagementSystem.response.Organization;

import com.tms.TaskManagementSystem.response.Worker.WorkerForOrganizationResponse;

import java.util.List;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrganizationResponse {
    private String name;
    private List<WorkerForOrganizationResponse> workers;
    private boolean isDeleted;
}