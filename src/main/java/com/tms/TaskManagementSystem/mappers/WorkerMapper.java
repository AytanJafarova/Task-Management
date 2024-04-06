package com.tms.TaskManagementSystem.mappers;

import com.tms.TaskManagementSystem.entity.Worker;
import com.tms.TaskManagementSystem.dto.WorkerDTO;
import com.tms.TaskManagementSystem.request.Worker.CreateWorkerRequest;
import com.tms.TaskManagementSystem.request.Worker.UpdateWorkerRequest;
import com.tms.TaskManagementSystem.response.Worker.WorkerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WorkerMapper {
    WorkerMapper INSTANCE = Mappers.getMapper(WorkerMapper.class);

    WorkerResponse workerToWorkerResponse(Worker worker);
    WorkerDTO workerToWorkerDTO(Worker worker);
    Worker createWorkerRequestToWorker(CreateWorkerRequest request);
    Worker updateWorkerRequestToWorker(UpdateWorkerRequest request);
}
