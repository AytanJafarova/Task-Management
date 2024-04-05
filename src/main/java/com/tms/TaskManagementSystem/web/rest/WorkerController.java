package com.tms.TaskManagementSystem.web.rest;

import com.tms.TaskManagementSystem.request.Worker.CreateWorkerRequest;
import com.tms.TaskManagementSystem.request.Worker.UpdateWorkerOrganizationRequest;
import com.tms.TaskManagementSystem.request.Worker.UpdateWorkerRequest;
import com.tms.TaskManagementSystem.response.Worker.WorkerGetResponse;
import com.tms.TaskManagementSystem.response.Worker.WorkerResponse;
import com.tms.TaskManagementSystem.services.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/worker")
@RequiredArgsConstructor
public class WorkerController {
    private final WorkerService workerService;

    @PostMapping("/addWorker")
    @ResponseStatus(HttpStatus.CREATED)
    public WorkerResponse addWorker(@RequestBody CreateWorkerRequest request)
    {
        return workerService.AddWorker(request);
    }
    @GetMapping("/getWorkers")
    public List<WorkerGetResponse> getWorkers()
    {
        return workerService.GetWorkers();
    }

    @GetMapping("/getWorker/{id}")
    public WorkerGetResponse getWorkerById(@PathVariable Long id)
    {
        return workerService.GetWorkerById(id);
    }
    @GetMapping("/getWorkerByOrganizationId/{id}")
    public List<WorkerGetResponse> getWorkersByOrganizationId(@PathVariable Long id)
    {
        return  workerService.GetWorkersByOrganizationId(id);
    }
    @PutMapping("/updateWorker/{id}")
    public WorkerResponse updateWorker(@PathVariable Long id, @RequestBody UpdateWorkerRequest request)
    {
        return workerService.UpdateWorker(id,request);
    }
    @PutMapping("/changeWorkerOrganization/{id}")
    public WorkerResponse changeWorkerOrganization(@PathVariable Long id, @RequestBody UpdateWorkerOrganizationRequest request)
    {
        return workerService.ChangeOrganization(id,request);
    }
    @DeleteMapping("/softDeleteWorker/{id}")
    public boolean softDeleteWorker(@PathVariable Long id)
    {
        return workerService.SoftDeleteWorker(id);
    }

    @DeleteMapping("/hardDeleteWorker/{id}")
    public boolean hardDeleteWorker(@PathVariable Long id)
    {
        return workerService.HardDeleteWorker(id);
    }
}
