package com.tms.TaskManagementSystem.mappers;

import com.tms.TaskManagementSystem.entity.Task;
import com.tms.TaskManagementSystem.dto.TaskDTO;
import com.tms.TaskManagementSystem.request.Task.*;
import com.tms.TaskManagementSystem.response.Task.TaskResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);
    TaskResponse taskToTaskResponse(Task task);
    TaskDTO taskToTaskDTO(Task task);
    Task createTaskRequestToTask(CreateTaskRequest taskRequest);
    Task updateTaskRequestToTask(UpdateTaskRequest taskRequest);
}