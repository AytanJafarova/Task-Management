package com.tms.TaskManagementSystem.request.Task;

import com.tms.TaskManagementSystem.domain.Worker;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignTaskRequest {
    private Long id;
}
