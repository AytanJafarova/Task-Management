package com.tms.TaskManagementSystem.services.impl;

import com.tms.TaskManagementSystem.entity.Organization;
import com.tms.TaskManagementSystem.entity.Worker;
import com.tms.TaskManagementSystem.entity.enums.OrganizationStatus;
import com.tms.TaskManagementSystem.entity.enums.WorkerStatus;
import com.tms.TaskManagementSystem.exception.DataNotFoundException;
import com.tms.TaskManagementSystem.exception.IllegalArgumentException;
import com.tms.TaskManagementSystem.mappers.OrganizationMapper;
import com.tms.TaskManagementSystem.mappers.WorkerMapper;
import com.tms.TaskManagementSystem.repository.OrganizationRepository;
import com.tms.TaskManagementSystem.repository.WorkerRepository;
import com.tms.TaskManagementSystem.request.Worker.CreateWorkerRequest;
import com.tms.TaskManagementSystem.request.Worker.UpdateWorkerRequest;
import com.tms.TaskManagementSystem.response.Worker.WorkerResponse;
import com.tms.TaskManagementSystem.services.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkerServiceImpl implements WorkerService {
    private final WorkerRepository workerRepository;
    private final OrganizationRepository organizationRepository;

    boolean checkingUserCredentials(String username, String password, boolean ignoreUsername) {
        Optional<Worker> workerSelect = workerRepository.findByUsername(username.toLowerCase());
        return !username.isBlank() && !password.isBlank() && password.length() >= 8 && workerSelect.isEmpty();
    }
    @Override
    public WorkerResponse save(CreateWorkerRequest request) {
        Optional<Organization> organizationWorker = organizationRepository.findByIdAndStatus(request.getOrganization_id(), OrganizationStatus.ACTIVE);
        if(checkingUserCredentials(request.getUsername(),request.getPassword(),false))
        {
            if(organizationWorker.isPresent())
            {
                Worker worker = workerRepository.save(Worker.builder()
                        .name(request.getName())
                        .surname(request.getSurname())
                        .email(request.getEmail())
                        .password(request.getPassword())
                        .username(request.getUsername().toLowerCase())
                        .status(WorkerStatus.ACTIVE)
                        .organization(organizationWorker.get()).build());
                return WorkerResponse.builder()
                        .username(worker.getUsername().toLowerCase())
                        .email(worker.getEmail())
                        .password(worker.getPassword())
                        .name(worker.getName())
                        .surname(worker.getSurname())
                        .status(worker.getStatus())
                        .id(worker.getId())
                        .organization(OrganizationMapper.INSTANCE.organizationToOrganizationDTO(organizationWorker.get()))
                        .build();
            }
            else{
                throw new DataNotFoundException("Organization with id of "+request.getOrganization_id()+" is not found!");
            }
        }
        else{
            throw new IllegalArgumentException("Invalid operation!");
        }
    }

    @Override
    public WorkerResponse update(Long id,UpdateWorkerRequest request) {
        Optional<Worker> selectedWorker = workerRepository.findById(id);
        boolean ignoreUsername = false;
        if(selectedWorker.isPresent())
        {
            if(selectedWorker.get().getUsername().equalsIgnoreCase(request.getUsername()))
            {
                ignoreUsername = true;
            }
            if(checkingUserCredentials(request.getUsername(),request.getPassword(),ignoreUsername))
            {
                selectedWorker.get().setName(request.getName());
                selectedWorker.get().setSurname(request.getSurname());
                selectedWorker.get().setUsername(request.getUsername().toLowerCase());
                selectedWorker.get().setPassword(request.getPassword());
                selectedWorker.get().setEmail(request.getEmail());
                workerRepository.save(selectedWorker.get());
                return WorkerMapper.INSTANCE.workerToWorkerResponse(selectedWorker.get());
            }
            else{
                throw new IllegalArgumentException("Invalid operation!");
            }
        }
        throw new DataNotFoundException("Worker with id of " + id + " is not found!");
    }
    @Override
    public boolean delete(Long id) {
        Optional<Worker> selectedWorker = workerRepository.findById(id);
        if(selectedWorker.isPresent())
        {
            workerRepository.delete(selectedWorker.get());
            return true;
        }
        else{
            throw new DataNotFoundException("Worker with id of "+ id+" is not found!");
        }
    }

    @Override
    public boolean inactivate(Long id) {
        Optional<Worker> selectedWorker = workerRepository.findByIdAndStatus(id,WorkerStatus.ACTIVE);
        if(selectedWorker.isPresent())
        {
            selectedWorker.get().setStatus(WorkerStatus.INACTIVE);
            workerRepository.save(selectedWorker.get());
            return true;
        }
        else{
            throw new DataNotFoundException("Worker with id of "+ id+" is not found!");
        }
    }
    @Override
    public List<WorkerResponse> getWorkers(int pgNum,int pgSize) {
        Pageable pageable = PageRequest.of(pgNum, pgSize);
        Page<Worker> workers = workerRepository.findAll(pageable);
        List<WorkerResponse> workerResponses = new ArrayList<>();
        for(Worker worker : workers)
        {
            WorkerResponse workerGetResponse = WorkerMapper.INSTANCE.workerToWorkerResponse(worker);
            workerResponses.add(workerGetResponse);
        }
        return workerResponses;
    }

    @Override
    public List<WorkerResponse> getActiveWorkers(int pgNum,int pgSize) {
        Pageable pageable = PageRequest.of(pgNum, pgSize);
        List<Worker> workers = workerRepository.findByStatus(WorkerStatus.ACTIVE,pageable);
        List<WorkerResponse> workerResponses = new ArrayList<>();
        for(Worker worker : workers)
        {
            WorkerResponse workerGetResponse = WorkerMapper.INSTANCE.workerToWorkerResponse(worker);
            workerResponses.add(workerGetResponse);
        }
        return workerResponses;
    }

    @Override
    public WorkerResponse getWorkerById(Long id) {
        Optional<Worker> worker = workerRepository.findByIdAndStatus(id, WorkerStatus.ACTIVE);
        if(worker.isPresent())
        {
            return WorkerMapper.INSTANCE.workerToWorkerResponse(worker.get());
        }
        else{
            throw new DataNotFoundException("Worker with id of " + id + " is not found!");
        }
    }

    @Override
    public List<WorkerResponse> getWorkersByOrganizationId(Long id,int pgNum,int pgSize) {
        Optional<Organization> organizationOptional = organizationRepository.findByIdAndStatus(id,OrganizationStatus.ACTIVE);
        if(organizationOptional.isPresent())
        {
            Pageable pageable = PageRequest.of(pgNum, pgSize);
            List<Worker> workers = workerRepository.findByOrganizationIdAndStatus(id,WorkerStatus.ACTIVE,pageable);
            List<WorkerResponse> workerGetResponses = new ArrayList<>();
            for(Worker worker : workers)
            {
                WorkerResponse workerGetResponse = WorkerMapper.INSTANCE.workerToWorkerResponse(worker);
                workerGetResponses.add(workerGetResponse);
            }
            return workerGetResponses;
        }
        else{
            throw new IllegalArgumentException("Organization with id of "+id+" is not found");
        }
    }
}
