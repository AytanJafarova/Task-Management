package com.tms.TaskManagementSystem.services;

import com.tms.TaskManagementSystem.request.Task.*;
import com.tms.TaskManagementSystem.response.Task.TaskListResponse;
import com.tms.TaskManagementSystem.response.Task.TaskResponse;
import org.springframework.data.domain.Pageable;

public interface TaskService {
    TaskResponse save(CreateTaskRequest request);
    TaskResponse update(Long id,UpdateTaskRequest request);
    TaskResponse assign(Long id,Long workerId);
    TaskResponse close(Long id);
    boolean delete(Long id);
    boolean arrived(Long id);
    TaskListResponse getTasks(Pageable pageable);
    TaskListResponse getInprogress(Pageable pageable);
    TaskListResponse getClosed(Pageable pageable);
    TaskListResponse getTodo(Pageable pageable);

    TaskResponse getTaskById(Long id);
    TaskListResponse getByWorkerId(Long id,Pageable pageable);
}