package com.tms.TaskManagementSystem.services;

import com.tms.TaskManagementSystem.request.Worker.CreateWorkerRequest;
import com.tms.TaskManagementSystem.request.Worker.UpdateWorkerRequest;
import com.tms.TaskManagementSystem.response.Worker.WorkerResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WorkerService {
    WorkerResponse save(CreateWorkerRequest request);
    WorkerResponse update(Long id,UpdateWorkerRequest request);
    boolean delete(Long id);
    boolean inactivate(Long id);
    List<WorkerResponse> getWorkers(Pageable pageable);
    List<WorkerResponse> getActiveWorkers(Pageable pageable);
    WorkerResponse getWorkerById(Long id);
    List<WorkerResponse> getWorkersByOrganizationId(Long id, Pageable pageable);
}
