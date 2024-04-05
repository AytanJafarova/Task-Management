package com.tms.TaskManagementSystem.request.Task;

import com.tms.TaskManagementSystem.enums.TaskDegree;
import com.tms.TaskManagementSystem.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDetailsRequest {
    private String header;
    private LocalDateTime created;
    private LocalDateTime deadline;
    private LocalDateTime closed;
}
