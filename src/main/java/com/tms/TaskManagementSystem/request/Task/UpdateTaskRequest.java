package com.tms.TaskManagementSystem.request.Task;

import java.time.LocalDateTime;

import com.tms.TaskManagementSystem.entity.enums.TaskPriority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskRequest {
    private String header;
    private String content;
    private LocalDateTime deadline;
    private TaskPriority priority;
}