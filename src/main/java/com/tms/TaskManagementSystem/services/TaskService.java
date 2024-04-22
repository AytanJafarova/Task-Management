package com.tms.TaskManagementSystem.services;

import com.tms.TaskManagementSystem.entity.enums.TaskStatus;
import com.tms.TaskManagementSystem.request.Task.*;
import com.tms.TaskManagementSystem.response.Task.TaskListResponse;
import com.tms.TaskManagementSystem.response.Task.TaskResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {
    TaskResponse save(CreateTaskRequest request);
    TaskResponse update(Long id,UpdateTaskRequest request);
    TaskResponse assign(Long id,Long workerId);
    TaskResponse close(Long id);
    boolean delete(Long id);
    TaskListResponse getTasks(Pageable pageable);
    TaskListResponse getByTaskStatus(TaskStatus status,Pageable pageable);
    List<TaskResponse> getByTaskStatus(TaskStatus status);
    TaskResponse getTaskById(Long id);
    TaskListResponse getByWorkerId(Long id,Pageable pageable);
}