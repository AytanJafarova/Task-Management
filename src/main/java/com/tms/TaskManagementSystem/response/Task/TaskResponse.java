package com.tms.TaskManagementSystem.response.Task;

import com.tms.TaskManagementSystem.enums.TaskDegree;
import com.tms.TaskManagementSystem.enums.TaskStatus;
import com.tms.TaskManagementSystem.response.Worker.WorkerForTaskResponse;
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
    private String header;
    private String content;
    private TaskDegree degree;
    private TaskStatus status;
    private LocalDateTime created;
    private WorkerForTaskResponse worker;
}