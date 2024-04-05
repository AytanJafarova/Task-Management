package com.tms.TaskManagementSystem.request.Organization;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateOrganizationRequest {
    private String name;
}
