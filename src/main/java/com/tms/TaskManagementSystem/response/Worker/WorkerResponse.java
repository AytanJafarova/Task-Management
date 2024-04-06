package com.tms.TaskManagementSystem.response.Worker;

import java.util.List;

import com.tms.TaskManagementSystem.dto.OrganizationDTO;
import com.tms.TaskManagementSystem.dto.TaskDTO;
import com.tms.TaskManagementSystem.entity.enums.WorkerStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkerResponse {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String name;
    private String surname;
    private WorkerStatus status;
    private OrganizationDTO organization;
    private List<TaskDTO> tasks;
}