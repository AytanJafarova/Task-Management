package com.tms.TaskManagementSystem.mappers;

import com.tms.TaskManagementSystem.domain.Task;
import com.tms.TaskManagementSystem.request.Task.*;
import com.tms.TaskManagementSystem.response.Task.TaskForWorkerResponse;
import com.tms.TaskManagementSystem.response.Task.TaskResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);
    TaskForWorkerResponse taskToTaskForWorkerResponse(Task task);
    TaskResponse taskToTaskResponse(Task task);
    Task assignTaskRequestToTask(AssignTaskRequest taskRequest);
    Task closeTaskRequestToTask(CloseTaskRequest taskRequest);
    Task createTaskRequestToTask(CreateTaskRequest taskRequest);
    Task updateTaskRequestToTask(UpdateTaskRequest taskRequest);
    Task updateTaskStatusRequestToTask(UpdateTaskStatusRequest taskStatusRequest);
    Task updateTaskDegreeRequestToTask(UpdateTaskDegreeRequest taskDegreeRequest);
    Task taskDetailsRequestToTask(TaskDetailsRequest taskRequest);
}