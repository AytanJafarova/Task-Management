package com.tms.TaskManagementSystem.request.Task;

import com.tms.TaskManagementSystem.enums.TaskDegree;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskDegreeRequest {
    private int degree;
}
