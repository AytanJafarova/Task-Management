package com.tms.TaskManagementSystem.response.Organization;

import com.tms.TaskManagementSystem.dto.WorkerDTO;
import com.tms.TaskManagementSystem.entity.enums.OrganizationStatus;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrganizationResponse {
    private Long id;
    private String name;
    private OrganizationStatus status;
}
