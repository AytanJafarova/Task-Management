package com.tms.TaskManagementSystem.response.Task;

import com.tms.TaskManagementSystem.dto.WorkerDTO;
import com.tms.TaskManagementSystem.entity.enums.TaskPriority;
import com.tms.TaskManagementSystem.entity.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private Long id;
    private String header;
    private String content;
    private TaskPriority priority;
    private TaskStatus status;
    private LocalDateTime created;
    private LocalDateTime deadline;
    private LocalDateTime closed;
    private WorkerDTO worker;
}