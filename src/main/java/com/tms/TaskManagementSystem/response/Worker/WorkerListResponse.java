package com.tms.TaskManagementSystem.response.Worker;

import com.tms.TaskManagementSystem.utils.PaginationInfo;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WorkerListResponse {
    private List<WorkerResponse> items;
    private PaginationInfo paginationInfo;
}
