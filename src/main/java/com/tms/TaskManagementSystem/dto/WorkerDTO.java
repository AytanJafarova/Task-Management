package com.tms.TaskManagementSystem.dto;

import com.tms.TaskManagementSystem.entity.enums.WorkerStatus;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WorkerDTO {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String surname;
    private String email;
    private WorkerStatus status;
}
