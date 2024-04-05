package com.tms.TaskManagementSystem.services;

import com.tms.TaskManagementSystem.request.Worker.CreateWorkerRequest;
import com.tms.TaskManagementSystem.request.Worker.UpdateWorkerOrganizationRequest;
import com.tms.TaskManagementSystem.request.Worker.UpdateWorkerRequest;
import com.tms.TaskManagementSystem.response.Worker.WorkerGetResponse;
import com.tms.TaskManagementSystem.response.Worker.WorkerResponse;

import java.util.List;

public interface WorkerService {
    WorkerResponse AddWorker(CreateWorkerRequest request);
    WorkerResponse UpdateWorker(Long id,UpdateWorkerRequest request);
    WorkerResponse ChangeOrganization(Long id, UpdateWorkerOrganizationRequest request);
    boolean HardDeleteWorker(Long id);
    boolean SoftDeleteWorker(Long id);
    List<WorkerGetResponse> GetWorkers();
    WorkerGetResponse GetWorkerById(Long id);
    List<WorkerGetResponse> GetWorkersByOrganizationId(Long id);
}
