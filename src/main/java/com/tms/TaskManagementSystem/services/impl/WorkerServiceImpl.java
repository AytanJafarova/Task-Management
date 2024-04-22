package com.tms.TaskManagementSystem.services.impl;

import com.tms.TaskManagementSystem.entity.Organization;
import com.tms.TaskManagementSystem.entity.Worker;
import com.tms.TaskManagementSystem.entity.enums.OrganizationStatus;
import com.tms.TaskManagementSystem.entity.enums.Role;
import com.tms.TaskManagementSystem.entity.enums.WorkerStatus;
import com.tms.TaskManagementSystem.exception.DataNotFoundException;
import com.tms.TaskManagementSystem.exception.IllegalArgumentException;
import com.tms.TaskManagementSystem.exception.response.ResponseMessage;
import com.tms.TaskManagementSystem.mappers.WorkerMapper;
import com.tms.TaskManagementSystem.repository.OrganizationRepository;
import com.tms.TaskManagementSystem.repository.WorkerRepository;
import com.tms.TaskManagementSystem.request.Worker.UpdateWorkerRequest;
import com.tms.TaskManagementSystem.response.Worker.WorkerListResponse;
import com.tms.TaskManagementSystem.response.Worker.WorkerResponse;
import com.tms.TaskManagementSystem.services.WorkerService;
import com.tms.TaskManagementSystem.utils.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkerServiceImpl implements WorkerService {
    private final WorkerRepository workerRepository;
    private final OrganizationRepository organizationRepository;

    boolean checkingUserCredentials(String username, String password, boolean ignoreUsername) {
        if (username.isBlank() || password.isBlank() || password.length() < 8) {
            throw new IllegalArgumentException(ResponseMessage.ERROR_INVALID_USERNAME_PASSWORD);
        }
        workerRepository.findByUsername(username.toLowerCase())
                .ifPresent(worker -> {
                    throw new IllegalArgumentException(ResponseMessage.ERROR_USERNAME_EXISTS);
                });

        return true;
    }

    @Override
    public WorkerResponse update(Long id,UpdateWorkerRequest request) {
        Worker selectedWorker = workerRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException(ResponseMessage.ERROR_WORKER_NOT_FOUND_BY_ID));

        boolean ignoreUsername = selectedWorker.getUsername().equalsIgnoreCase(request.getUsername());
        if(checkingUserCredentials(request.getUsername(),request.getPassword(),ignoreUsername))
        {
            selectedWorker.setName(request.getName());
            selectedWorker.setSurname(request.getSurname());
            selectedWorker.setUsername(request.getUsername().toLowerCase());
            selectedWorker.setPassword(request.getPassword());
            selectedWorker.setEmail(request.getEmail());
            workerRepository.save(selectedWorker);
            return WorkerMapper.INSTANCE.workerToWorkerResponse(selectedWorker);
        }
        else{
            throw new IllegalArgumentException(ResponseMessage.ERROR_INVALID_OPERATION);
        }
    }
    @Override
    public boolean delete(Long id) {
        Worker selectedWorker = workerRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException(ResponseMessage.ERROR_WORKER_NOT_FOUND_BY_ID));
        workerRepository.delete(selectedWorker);
        return true;
    }

    @Override
    public boolean inactivate(Long id) {
        Worker selectedWorker = workerRepository.findByIdAndStatus(id,WorkerStatus.ACTIVE)
                .orElseThrow(()->new DataNotFoundException(ResponseMessage.ERROR_WORKER_NOT_FOUND_BY_ID));

            selectedWorker.setStatus(WorkerStatus.INACTIVE);
            workerRepository.save(selectedWorker);
            return true;
    }
    @Override
    public WorkerListResponse getWorkers(Pageable pageable) {
        Page<Worker> workers = workerRepository.findByRole(Role.USER, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        WorkerListResponse response = WorkerListResponse.builder().build();
        response.setItems(workers.getContent().stream().map(WorkerMapper.INSTANCE::workerToWorkerResponse).collect(Collectors.toList()));
        response.setPaginationInfo(PaginationUtil.getPaginationInfo(workers));
        return response;
    }

    @Override
    public WorkerListResponse getActiveWorkers(Pageable pageable) {
        Page<Worker> workers = workerRepository.findByStatusAndRole(WorkerStatus.ACTIVE, Role.USER ,PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        WorkerListResponse response = WorkerListResponse.builder().build();
        response.setItems(workers.getContent().stream().map(WorkerMapper.INSTANCE::workerToWorkerResponse).collect(Collectors.toList()));
        response.setPaginationInfo(PaginationUtil.getPaginationInfo(workers));
        return response;
    }

    @Override
    public WorkerResponse getWorkerById(Long id) {
        Worker worker = workerRepository.findByIdAndStatus(id, WorkerStatus.ACTIVE)
                .orElseThrow(()->new DataNotFoundException(ResponseMessage.ERROR_WORKER_NOT_FOUND_BY_ID));
        return WorkerMapper.INSTANCE.workerToWorkerResponse(worker);
    }

    @Override
    public WorkerListResponse getWorkersByOrganizationId(Long id,Pageable pageable) {
        Organization organizationOptional = organizationRepository.findByIdAndStatus(id,OrganizationStatus.ACTIVE)
                .orElseThrow(()->new DataNotFoundException(ResponseMessage.ERROR_ORGANIZATION_NOT_FOUND_BY_ID));
        Page<Worker> workers = workerRepository.findByOrganizationIdAndStatus(organizationOptional.getId(),WorkerStatus.ACTIVE,PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        WorkerListResponse response = WorkerListResponse.builder().build();
        response.setItems(workers.getContent().stream().map(WorkerMapper.INSTANCE::workerToWorkerResponse).collect(Collectors.toList()));
        response.setPaginationInfo(PaginationUtil.getPaginationInfo(workers));
        return response;
    }
}
