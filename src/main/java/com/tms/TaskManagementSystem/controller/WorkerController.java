package com.tms.TaskManagementSystem.controller;

import com.tms.TaskManagementSystem.request.Worker.CreateWorkerRequest;
import com.tms.TaskManagementSystem.request.Worker.UpdateWorkerRequest;
import com.tms.TaskManagementSystem.response.Worker.WorkerResponse;
import com.tms.TaskManagementSystem.services.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workers")
@RequiredArgsConstructor
//@Api("Worker Operations")

public class WorkerController {
    private final WorkerService workerService;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
//    @ApiOperation(value = "Adding new Worker")
    public WorkerResponse save(@RequestBody CreateWorkerRequest request)
    {
        return workerService.save(request);
    }
    @GetMapping("/all")
//    @ApiOperation(value = "Find all workers")
    public ResponseEntity<List<WorkerResponse>> getWorkers(@RequestParam(defaultValue = "0") int pageNumber,@RequestParam(defaultValue = "10") int pageSize)
    {
        return ResponseEntity.ok(workerService.getWorkers(pageNumber, pageSize));
    }

    @GetMapping("")
//    @ApiOperation(value = "Find active workers")
    public ResponseEntity<List<WorkerResponse>> getActiveWorkers(@RequestParam(defaultValue = "0") int pageNumber,@RequestParam(defaultValue = "10") int pageSize)
    {
        return ResponseEntity.ok(workerService.getActiveWorkers(pageNumber, pageSize));
    }

    @GetMapping("/{id}")
//    @ApiOperation(value = "Find Worker by worker id")
    public ResponseEntity<WorkerResponse> getWorkerById(@PathVariable Long id)
    {
        return ResponseEntity.ok(workerService.getWorkerById(id));
    }
    @GetMapping("/organization/{id}")
//    @ApiOperation(value = "Find Workers by organization id")
    public ResponseEntity<List<WorkerResponse>> getWorkersByOrganizationId(@PathVariable Long id,@RequestParam(defaultValue = "0") int pageNumber,@RequestParam(defaultValue = "10") int pageSize)
    {
        return ResponseEntity.ok(workerService.getWorkersByOrganizationId(id,pageNumber, pageSize));
    }
    @PutMapping("/update/{id}")
//    @ApiOperation(value = "Update Worker by worker id")
    public ResponseEntity<WorkerResponse> update(@PathVariable Long id, @RequestBody UpdateWorkerRequest request)
    {
        return ResponseEntity.ok(workerService.update(id,request));
    }
    @PutMapping("/inactivate/{id}")
//    @ApiOperation(value = "Inactivate Worker by worker id")
    public ResponseEntity<Boolean> inactivate(@PathVariable Long id)
    {
        return ResponseEntity.ok(workerService.inactivate(id));
    }

    @DeleteMapping("/delete/{id}")
//    @ApiOperation(value = "Delete Worker by worker id")
    public ResponseEntity<Boolean> delete(@PathVariable Long id)
    {
        return ResponseEntity.ok(workerService.delete(id));
    }
}
