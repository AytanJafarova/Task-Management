package com.tms.TaskManagementSystem.controller;

import com.tms.TaskManagementSystem.request.Task.*;
import com.tms.TaskManagementSystem.response.Task.TaskResponse;
import com.tms.TaskManagementSystem.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creating new Task")
    public ResponseEntity<TaskResponse> save(@RequestBody CreateTaskRequest request)
    {
        return ResponseEntity.ok(taskService.save(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find Task by task id")
    public ResponseEntity<TaskResponse> getTaskByID(@PathVariable Long id)
    {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @GetMapping()
    @Operation(summary = "Find all tasks")
    public ResponseEntity<List<TaskResponse>> getTasks(@RequestParam(defaultValue = "0") int pageNumber,@RequestParam(defaultValue = "10") int pageSize)
    {
        return ResponseEntity.ok(taskService.getTasks(pageNumber, pageSize));
    }

    @GetMapping("/worker/{id}")
    @Operation(summary = "Find Tasks by worker id")
    public ResponseEntity<List<TaskResponse>> getByWorkerID(@PathVariable Long id,@RequestParam(defaultValue = "0") int pageNumber,@RequestParam(defaultValue = "10") int pageSize)
    {
        return ResponseEntity.ok(taskService.getByWorkerId(id, pageNumber, pageSize));
    }

    @GetMapping("/closed")
    @Operation(summary = "Find all closed tasks")
    public ResponseEntity<List<TaskResponse>> getClosedTasks(@RequestParam(defaultValue = "0") int pageNumber,@RequestParam(defaultValue = "10") int pageSize)
    {
        return ResponseEntity.ok(taskService.getClosed(pageNumber, pageSize));
    }

    @GetMapping("/progress")
    @Operation(summary = "Find all Tasks in progress")
    public ResponseEntity<List<TaskResponse>> getInProgress(@RequestParam(defaultValue = "0") int pageNumber,@RequestParam(defaultValue = "10") int pageSize)
    {
        return ResponseEntity.ok(taskService.getInprogress(pageNumber, pageSize));
    }

    @GetMapping("/todo")
    @Operation(summary = "Find all Tasks in ToDo")
    public ResponseEntity<List<TaskResponse>> getTodo(@RequestParam(defaultValue = "0") int pageNumber,@RequestParam(defaultValue = "10") int pageSize)
    {
        return ResponseEntity.ok(taskService.getTodo(pageNumber, pageSize));
    }

    @GetMapping("/arrived/{id}")
    @Operation(summary = "Check if deadline arrived for task")
    public ResponseEntity<Boolean> arrived(@PathVariable Long id)
    {
        return ResponseEntity.ok(taskService.arrived(id));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete Task by task id")
    public ResponseEntity<Boolean> delete(@PathVariable Long id)
    {
        return ResponseEntity.ok(taskService.delete(id));
    }

    @PutMapping("/close/{id}")
    @Operation(summary = "Close Task")
    public ResponseEntity<TaskResponse> close(@PathVariable Long id)
    {
        return ResponseEntity.ok(taskService.close(id));
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update Task by task id")
    public ResponseEntity<TaskResponse> update(@PathVariable Long id,@RequestBody UpdateTaskRequest request)
    {
        return ResponseEntity.ok(taskService.update(id,request));
    }

    @PutMapping("/assign/{id}/{workerId}")
    @Operation(summary = "Assign Task to worker")
    public ResponseEntity<TaskResponse> assign(@PathVariable Long id,@PathVariable Long workerId)
    {
        return ResponseEntity.ok(taskService.assign(id,workerId));
    }
}
