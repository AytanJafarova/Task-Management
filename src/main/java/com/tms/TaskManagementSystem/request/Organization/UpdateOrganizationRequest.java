package com.tms.TaskManagementSystem.request.Organization;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateOrganizationRequest {
    private String name;
}
