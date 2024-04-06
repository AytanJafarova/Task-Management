package com.tms.TaskManagementSystem.services;

import com.tms.TaskManagementSystem.request.Worker.CreateWorkerRequest;
import com.tms.TaskManagementSystem.request.Worker.UpdateWorkerRequest;
import com.tms.TaskManagementSystem.response.Worker.WorkerResponse;

import java.util.List;

public interface WorkerService {
    WorkerResponse save(CreateWorkerRequest request);
    WorkerResponse update(Long id,UpdateWorkerRequest request);
    boolean delete(Long id);
    boolean inactivate(Long id);
    List<WorkerResponse> getWorkers(int pgNum,int pgSize);
    List<WorkerResponse> getActiveWorkers(int pgNum,int pgSize);
    WorkerResponse getWorkerById(Long id);
    List<WorkerResponse> getWorkersByOrganizationId(Long id,int pgNum,int pgSize);
}
