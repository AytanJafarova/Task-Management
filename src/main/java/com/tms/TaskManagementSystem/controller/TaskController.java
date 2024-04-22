package com.tms.TaskManagementSystem.controller;

import com.tms.TaskManagementSystem.entity.enums.TaskStatus;
import com.tms.TaskManagementSystem.request.Task.*;
import com.tms.TaskManagementSystem.response.Task.TaskListResponse;
import com.tms.TaskManagementSystem.response.Task.TaskResponse;
import com.tms.TaskManagementSystem.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/save")
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
    public ResponseEntity<TaskListResponse> getTasks(Pageable pageable)
    {
        return ResponseEntity.ok(taskService.getTasks(pageable));
    }

    @GetMapping("/worker/{id}")
    @Operation(summary = "Find Tasks by worker id")
    public ResponseEntity<TaskListResponse> getByWorkerID(@PathVariable Long id, Pageable pageable)
    {
        return ResponseEntity.ok(taskService.getByWorkerId(id,pageable));
    }

    @GetMapping("/closed")
    @Operation(summary = "Find all closed tasks")
    public ResponseEntity<TaskListResponse> getClosedTasks(Pageable pageable)
    {
        return ResponseEntity.ok(taskService.getByTaskStatus(TaskStatus.DONE ,pageable));
    }

    @GetMapping("/progress")
    @Operation(summary = "Find all Tasks in progress")
    public ResponseEntity<TaskListResponse> getInProgress(Pageable pageable)
    {
        return ResponseEntity.ok(taskService.getByTaskStatus(TaskStatus.IN_PROGRESS,pageable));
    }

    @GetMapping("/todo")
    @Operation(summary = "Find all Tasks ToDo")
    public ResponseEntity<TaskListResponse> getTodo(Pageable pageable)
    {
        return ResponseEntity.ok(taskService.getByTaskStatus(TaskStatus.TO_DO,pageable));
    }

    @GetMapping("/overdue")
    @Operation(summary = "Find all Tasks Overdue")
    public ResponseEntity<TaskListResponse> getOverdue(Pageable pageable)
    {
        return ResponseEntity.ok(taskService.getByTaskStatus(TaskStatus.OVERDUE,pageable));
    }

    @GetMapping("/delayed")
    @Operation(summary = "Find all Tasks Delayed")
    public ResponseEntity<TaskListResponse> getDelayed(Pageable pageable)
    {
        return ResponseEntity.ok(taskService.getByTaskStatus(TaskStatus.DELAYED,pageable));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('admin:delete:task')")
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
