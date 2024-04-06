package com.tms.TaskManagementSystem.response.Organization;

import java.util.List;

import com.tms.TaskManagementSystem.dto.WorkerDTO;
import com.tms.TaskManagementSystem.entity.enums.OrganizationStatus;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrganizationWorkersResponse {
    private Long id;
    private String name;
    private List<WorkerDTO> workers;
    private OrganizationStatus status;
}