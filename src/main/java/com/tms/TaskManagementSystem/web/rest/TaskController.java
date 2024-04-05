package com.tms.TaskManagementSystem.web.rest;

import com.tms.TaskManagementSystem.request.Task.*;
import com.tms.TaskManagementSystem.response.Task.TaskForWorkerResponse;
import com.tms.TaskManagementSystem.response.Task.TaskResponse;
import com.tms.TaskManagementSystem.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/addTask")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse addTask(@RequestBody CreateTaskRequest request)
    {
        return taskService.AddTask(request);
    }

    @GetMapping("/getTask/{id}")
    public TaskResponse getTaskByID(@PathVariable Long id)
    {
        return taskService.GetTaskById(id);
    }

    @GetMapping("/getTasks")
    public List<TaskResponse> getTasks()
    {
        return taskService.GetTasks();
    }

    @GetMapping("/getTaskByHeader/{header}")
    public TaskResponse getTaskByHeader(@PathVariable String header)
    {
        return taskService.GetTaskByHeader(header.toLowerCase());
    }

    @GetMapping("/getTaskByWorkerId/{id}")
    public List<TaskForWorkerResponse> getTaskByWorkerID(@PathVariable Long id)
    {
        return taskService.GetTasksByWorkerId(id);
    }

    @GetMapping("/getClosedTasks")
    public List<TaskResponse> getClosedTasks()
    {
        return taskService.GetClosedTasks();
    }

    @GetMapping("/taskDeadlineArrived/{id}")
    public boolean deadlineArrived(@PathVariable Long id)
    {
        return taskService.DeadlineArrived(id);
    }

    @DeleteMapping("/deleteTask/{id}")
    public boolean deleteTask(@PathVariable Long id)
    {
        return taskService.DeleteTask(id);
    }

    @PutMapping("/closeTask/{id}")
    public TaskResponse closeTask(@PathVariable Long id)
    {
        return taskService.CloseTask(id);
    }

//    @PutMapping("/updateTaskStatus/{id}")
//    public TaskResponse updateTaskStatus(@PathVariable Long id,@RequestBody UpdateTaskStatusRequest status)
//    {
//        return taskService.UpdateTaskStatus(id,status);
//    }

    @PutMapping("/updateTaskDegree/{id}")
    public TaskResponse updateTaskDegree(@PathVariable Long id,@RequestBody UpdateTaskDegreeRequest degree)
    {
        return taskService.UpdateTaskDegree(id,degree);
    }

    @PutMapping("/updateTask/{id}")
    public TaskResponse updateTask(@PathVariable Long id,@RequestBody UpdateTaskRequest request)
    {
        return taskService.UpdateTask(id,request);
    }

    @PutMapping("/assignTask/{id}")
    public TaskResponse assignTask(@PathVariable Long id,@RequestBody AssignTaskRequest request)
    {
        return taskService.AssignTask(id,request);
    }
}
