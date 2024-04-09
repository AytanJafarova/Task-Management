package com.tms.TaskManagementSystem.response.Organization;

import com.tms.TaskManagementSystem.utils.PaginationInfo;
import lombok.*;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrganizationListResponse {
    private List<OrganizationResponse> items;
    private PaginationInfo paginationInfo;
}
