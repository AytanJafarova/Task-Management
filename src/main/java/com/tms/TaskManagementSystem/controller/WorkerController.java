package com.tms.TaskManagementSystem.controller;

import com.tms.TaskManagementSystem.request.Worker.UpdateWorkerRequest;
import com.tms.TaskManagementSystem.response.Worker.WorkerListResponse;
import com.tms.TaskManagementSystem.response.Worker.WorkerResponse;
import com.tms.TaskManagementSystem.services.WorkerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/workers")
@RequiredArgsConstructor
public class WorkerController {
    private final WorkerService workerService;

//    @PostMapping("/save")
//    @ResponseStatus(HttpStatus.CREATED)
//    @Operation(summary = "Adding new Worker")
//    public WorkerResponse save(@RequestBody CreateWorkerRequest request)
//    {
//        return workerService.save(request);
//    }
    @GetMapping("/all")
    @Operation(summary = "Find all workers")
    public ResponseEntity<WorkerListResponse> getWorkers(Pageable pageable)
    {
        return ResponseEntity.ok(workerService.getWorkers(pageable));
    }

    @GetMapping("")
    @Operation(summary = "Find active workers")
    public ResponseEntity<WorkerListResponse> getActiveWorkers(Pageable pageable)
    {
        return ResponseEntity.ok(workerService.getActiveWorkers(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find Worker by worker id")
    public ResponseEntity<WorkerResponse> getWorkerById(@PathVariable Long id)
    {
        return ResponseEntity.ok(workerService.getWorkerById(id));
    }
    @GetMapping("/organization/{id}")
    @Operation(summary = "Find Workers by organization id")
    public ResponseEntity<WorkerListResponse> getWorkersByOrganizationId(@PathVariable Long id,Pageable pageable)
    {
        return ResponseEntity.ok(workerService.getWorkersByOrganizationId(id,pageable));
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
