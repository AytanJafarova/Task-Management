package com.tms.TaskManagementSystem.response.Task;

import com.tms.TaskManagementSystem.enums.TaskDegree;
import com.tms.TaskManagementSystem.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskForWorkerResponse {
    private String header;
    private String content;
    private TaskDegree degree;
    private TaskStatus status;
}