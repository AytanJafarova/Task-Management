package com.tms.TaskManagementSystem.response.Organization;

import com.tms.TaskManagementSystem.response.Worker.WorkerForOrganizationResponse;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrganizationGetResponse {
    private String name;
    private List<WorkerForOrganizationResponse> worker;
}
