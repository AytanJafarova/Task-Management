package com.tms.TaskManagementSystem.services;

import com.tms.TaskManagementSystem.request.Task.*;
import com.tms.TaskManagementSystem.response.Task.TaskResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {
    TaskResponse save(CreateTaskRequest request);
    TaskResponse update(Long id,UpdateTaskRequest request);
    TaskResponse assign(Long id,Long workerId);
    TaskResponse close(Long id);
    boolean delete(Long id);
    boolean arrived(Long id);
    List<TaskResponse> getTasks(Pageable pageable);
    List<TaskResponse> getInprogress(Pageable pageable);
    List<TaskResponse> getClosed(Pageable pageable);
    List<TaskResponse> getTodo(Pageable pageable);

    TaskResponse getTaskById(Long id);
    List<TaskResponse> getByWorkerId(Long id,Pageable pageable);
}
