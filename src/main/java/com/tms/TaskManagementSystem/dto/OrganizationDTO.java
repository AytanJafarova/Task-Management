package com.tms.TaskManagementSystem.dto;

import com.tms.TaskManagementSystem.entity.enums.OrganizationStatus;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrganizationDTO {
    private Long id;
    private String name;
    private OrganizationStatus status;
}
