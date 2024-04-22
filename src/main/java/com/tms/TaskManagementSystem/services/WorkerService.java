package com.tms.TaskManagementSystem.services;

import com.tms.TaskManagementSystem.request.Worker.UpdateWorkerRequest;
import com.tms.TaskManagementSystem.response.Worker.WorkerListResponse;
import com.tms.TaskManagementSystem.response.Worker.WorkerResponse;
import org.springframework.data.domain.Pageable;

public interface WorkerService {
    WorkerResponse update(Long id,UpdateWorkerRequest request);
    boolean delete(Long id);
    boolean inactivate(Long id);
    WorkerListResponse getWorkers(Pageable pageable);
    WorkerListResponse getActiveWorkers(Pageable pageable);
    WorkerResponse getWorkerById(Long id);
    WorkerListResponse getWorkersByOrganizationId(Long id, Pageable pageable);
}
