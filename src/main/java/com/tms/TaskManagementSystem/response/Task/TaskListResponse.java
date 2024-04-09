package com.tms.TaskManagementSystem.response.Task;

import com.tms.TaskManagementSystem.utils.PaginationInfo;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TaskListResponse
{
    private List<TaskResponse> items;
    private PaginationInfo paginationInfo;
}
