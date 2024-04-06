package com.tms.TaskManagementSystem.services;

import com.tms.TaskManagementSystem.request.Task.*;
import com.tms.TaskManagementSystem.response.Task.TaskResponse;

import java.util.List;

public interface TaskService {
    TaskResponse save(CreateTaskRequest request);
    TaskResponse update(Long id,UpdateTaskRequest request);
    TaskResponse assign(Long id,Long workerId);
    TaskResponse close(Long id);
    boolean delete(Long id);
    boolean arrived(Long id);
    List<TaskResponse> getTasks(int pgNum,int pgSize);
    List<TaskResponse> getInprogress(int pgNum,int pgSize);
    List<TaskResponse> getClosed(int pgNum,int pgSize);
    List<TaskResponse> getTodo(int pgNum,int pgSize);

    TaskResponse getTaskById(Long id);
    List<TaskResponse> getByWorkerId(Long id,int pgNum,int pgSize);
}
