package com.tms.TaskManagementSystem.controller;

import com.tms.TaskManagementSystem.request.Worker.CreateWorkerRequest;
import com.tms.TaskManagementSystem.request.Worker.UpdateWorkerRequest;
import com.tms.TaskManagementSystem.response.Worker.WorkerResponse;
import com.tms.TaskManagementSystem.services.WorkerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workers")
@RequiredArgsConstructor
public class WorkerController {
    private final WorkerService workerService;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Adding new Worker")
    public WorkerResponse save(@RequestBody CreateWorkerRequest request)
    {
        return workerService.save(request);
    }
    @GetMapping("/all")
    @Operation(summary = "Find all workers")
    public ResponseEntity<List<WorkerResponse>> getWorkers(@RequestParam(defaultValue = "0") int pageNumber,@RequestParam(defaultValue = "10") int pageSize)
    {
        return ResponseEntity.ok(workerService.getWorkers(pageNumber, pageSize));
    }

    @GetMapping("")
    @Operation(summary = "Find active workers")
    public ResponseEntity<List<WorkerResponse>> getActiveWorkers(@RequestParam(defaultValue = "0") int pageNumber,@RequestParam(defaultValue = "10") int pageSize)
    {
        return ResponseEntity.ok(workerService.getActiveWorkers(pageNumber, pageSize));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find Worker by worker id")
    public ResponseEntity<WorkerResponse> getWorkerById(@PathVariable Long id)
    {
        return ResponseEntity.ok(workerService.getWorkerById(id));
    }
    @GetMapping("/organization/{id}")
    @Operation(summary = "Find Workers by organization id")
    public ResponseEntity<List<WorkerResponse>> getWorkersByOrganizationId(@PathVariable Long id,@RequestParam(defaultValue = "0") int pageNumber,@RequestParam(defaultValue = "10") int pageSize)
    {
        return ResponseEntity.ok(workerService.getWorkersByOrganizationId(id,pageNumber, pageSize));
    }
    @PutMapping("/update/{id}")
    @Operation(summary = "Update Worker by worker id")
    public ResponseEntity<WorkerResponse> update(@PathVariable Long id, @RequestBody UpdateWorkerRequest request)
    {
        return ResponseEntity.ok(workerService.update(id,request));
    }
    @PutMapping("/inactivate/{id}")
    @Operation(summary = "Inactivate Worker by worker id")
    public ResponseEntity<Boolean> inactivate(@PathVariable Long id)
    {
        return ResponseEntity.ok(workerService.inactivate(id));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete Worker by worker id")
    public ResponseEntity<Boolean> delete(@PathVariable Long id)
    {
        return ResponseEntity.ok(workerService.delete(id));
    }
}
