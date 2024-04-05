package com.tms.TaskManagementSystem.mappers;

import com.tms.TaskManagementSystem.domain.Worker;
import com.tms.TaskManagementSystem.request.Worker.CreateWorkerRequest;
import com.tms.TaskManagementSystem.request.Worker.UpdateWorkerOrganizationRequest;
import com.tms.TaskManagementSystem.request.Worker.UpdateWorkerRequest;
import com.tms.TaskManagementSystem.response.Worker.WorkerForOrganizationResponse;
import com.tms.TaskManagementSystem.response.Worker.WorkerForTaskResponse;
import com.tms.TaskManagementSystem.response.Worker.WorkerGetResponse;
import com.tms.TaskManagementSystem.response.Worker.WorkerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WorkerMapper {
    WorkerMapper INSTANCE = Mappers.getMapper(WorkerMapper.class);

    WorkerForOrganizationResponse workerToWorkerForOrganizationResponse(Worker worker);
    WorkerForTaskResponse workerToWorkerForTaskResponse(Worker worker);
    WorkerGetResponse workerToWorkerGetResponse(Worker worker);
    WorkerResponse workerToWorkerResponse(Worker worker);
    Worker createWorkerRequestToWorker(CreateWorkerRequest request);
    Worker updateWorkerRequestToWorker(UpdateWorkerRequest request);
    Worker updateWorkerOrganizationRequestToWorker(UpdateWorkerOrganizationRequest request);
}
