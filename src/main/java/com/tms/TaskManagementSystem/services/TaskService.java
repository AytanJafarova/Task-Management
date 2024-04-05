package com.tms.TaskManagementSystem.services;

import com.tms.TaskManagementSystem.request.Task.*;
import com.tms.TaskManagementSystem.response.Task.TaskForWorkerResponse;
import com.tms.TaskManagementSystem.response.Task.TaskResponse;

import java.util.List;

public interface TaskService {
    TaskResponse AddTask(CreateTaskRequest request);
    TaskResponse UpdateTask(Long id,UpdateTaskRequest request);
    TaskResponse AssignTask(Long id,AssignTaskRequest request);
    TaskResponse UpdateTaskDegree(Long id,UpdateTaskDegreeRequest request);
//    TaskResponse UpdateTaskStatus(Long id,UpdateTaskStatusRequest request);
    TaskResponse CloseTask(Long id);
    boolean DeleteTask(Long id);
    boolean DeadlineArrived(Long id);
    List<TaskResponse> GetTasks();
    TaskResponse GetTaskById(Long id);
    TaskResponse GetTaskByHeader(String header);
    List<TaskForWorkerResponse> GetTasksByWorkerId(Long id);
    List<TaskResponse> GetClosedTasks();
}
