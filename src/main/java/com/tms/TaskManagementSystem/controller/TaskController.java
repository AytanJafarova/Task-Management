package com.tms.TaskManagementSystem.controller;

import com.tms.TaskManagementSystem.request.Task.*;
import com.tms.TaskManagementSystem.response.Task.TaskResponse;
import com.tms.TaskManagementSystem.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
//@Api("Task Operations")
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
//    @ApiOperation(value = "Creating new Task")
    public TaskResponse save(@RequestBody CreateTaskRequest request)
    {
        return taskService.save(request);
    }

    @GetMapping("/{id}")
//    @ApiOperation(value = "Find Task by task id")
    public TaskResponse getTaskByID(@PathVariable Long id)
    {
        return taskService.getTaskById(id);
    }

    @GetMapping()
//    @ApiOperation(value = "Find all tasks")
    public List<TaskResponse> getTasks(@RequestParam(defaultValue = "0") int pageNumber,@RequestParam(defaultValue = "10") int pageSize)
    {
        return taskService.getTasks(pageNumber, pageSize);
    }

    @GetMapping("/worker/{id}")
//    @ApiOperation(value = "Find Tasks by worker id")
    public List<TaskResponse> getByWorkerID(@PathVariable Long id,@RequestParam(defaultValue = "0") int pageNumber,@RequestParam(defaultValue = "10") int pageSize)
    {
        return taskService.getByWorkerId(id, pageNumber, pageSize);
    }

    @GetMapping("/closed")
//    @ApiOperation(value = "Find all closed tasks")
    public List<TaskResponse> getClosedTasks(@RequestParam(defaultValue = "0") int pageNumber,@RequestParam(defaultValue = "10") int pageSize)
    {
        return taskService.getClosed(pageNumber, pageSize);
    }

    @GetMapping("/progress")
//    @ApiOperation(value = "Find all Tasks in progress")
    public List<TaskResponse> getInProgress(@RequestParam(defaultValue = "0") int pageNumber,@RequestParam(defaultValue = "10") int pageSize)
    {
        return taskService.getInprogress(pageNumber, pageSize);
    }

    @GetMapping("/todo")
//    @ApiOperation(value = "Find all Tasks in ToDo")
    public List<TaskResponse> getTodo(@RequestParam(defaultValue = "0") int pageNumber,@RequestParam(defaultValue = "10") int pageSize)
    {
        return taskService.getTodo(pageNumber, pageSize);
    }

    @GetMapping("/arrived/{id}")
//    @ApiOperation(value = "Check if deadline arrived for task")
    public boolean arrived(@PathVariable Long id)
    {
        return taskService.arrived(id);
    }

    @DeleteMapping("/delete/{id}")
//    @ApiOperation(value = "Delete Task by task id")
    public boolean delete(@PathVariable Long id)
    {
        return taskService.delete(id);
    }

    @PutMapping("/close/{id}")
//    @ApiOperation(value = "Close Task")
    public TaskResponse close(@PathVariable Long id)
    {
        return taskService.close(id);
    }

    @PutMapping("/update/{id}")
//    @ApiOperation(value = "Update Task by task id")
    public TaskResponse update(@PathVariable Long id,@RequestBody UpdateTaskRequest request)
    {
        return taskService.update(id,request);
    }

    @PutMapping("/assign/{id}/{workerId}")
//    @ApiOperation(value = "Assign Task to worker")
    public TaskResponse assign(@PathVariable Long id,@PathVariable Long workerId)
    {
        return taskService.assign(id,workerId);
    }
}
