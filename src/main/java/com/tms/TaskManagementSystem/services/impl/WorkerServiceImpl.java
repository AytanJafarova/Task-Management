package com.tms.TaskManagementSystem.services.impl;

import com.tms.TaskManagementSystem.domain.Organization;
import com.tms.TaskManagementSystem.domain.Worker;
import com.tms.TaskManagementSystem.exception.DataNotFoundException;
import com.tms.TaskManagementSystem.exception.IllegalArgumentException;
import com.tms.TaskManagementSystem.mappers.OrganizationMapper;
import com.tms.TaskManagementSystem.mappers.WorkerMapper;
import com.tms.TaskManagementSystem.repository.OrganizationRepository;
import com.tms.TaskManagementSystem.repository.WorkerRepository;
import com.tms.TaskManagementSystem.request.Worker.CreateWorkerRequest;
import com.tms.TaskManagementSystem.request.Worker.UpdateWorkerOrganizationRequest;
import com.tms.TaskManagementSystem.request.Worker.UpdateWorkerRequest;
import com.tms.TaskManagementSystem.response.Worker.WorkerGetResponse;
import com.tms.TaskManagementSystem.response.Worker.WorkerResponse;
import com.tms.TaskManagementSystem.services.WorkerService;
import lombok.RequiredArgsConstructor;
import org.hibernate.jdbc.Work;
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
        List<Worker> workers = workerRepository.findAll();
        boolean result = true;
        if (!username.isBlank() && !password.isBlank()) {
            for (Worker worker : workers) {
                if ((!ignoreUsername && worker.getUsername().equalsIgnoreCase(username)) || password.trim().length() < 8) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }
    @Override
    public WorkerResponse AddWorker(CreateWorkerRequest request) {
        Optional<Organization> organizationWorker = organizationRepository.findById(request.getOrganization_id());
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
                        .organization(organizationWorker.get()).build());
                return WorkerResponse.builder()
                        .username(worker.getUsername().toLowerCase())
                        .email(worker.getEmail())
                        .password(worker.getPassword())
                        .organization(OrganizationMapper.INSTANCE.organizationToOrganizationForWorkerResponse(organizationWorker.get()))
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
    public WorkerResponse UpdateWorker(Long id,UpdateWorkerRequest request) {
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
    public WorkerResponse ChangeOrganization(Long id, UpdateWorkerOrganizationRequest request) {
        Optional<Worker> selectedWorker = workerRepository.findById(id);
        List<Long> list_ids = organizationRepository.findAllOrganizationIds();
        Optional<Organization> newOrganization = organizationRepository.findById(request.getOrganization_id());
        if(selectedWorker.isPresent())
        {
            if(list_ids.contains(request.getOrganization_id()))
            {
                selectedWorker.get().setOrganization(newOrganization.get());
                return WorkerMapper.INSTANCE.workerToWorkerResponse(selectedWorker.get());
            }
            else{
                throw new DataNotFoundException("Organization with id of "+request.getOrganization_id()+" not found!");
            }
        }
        else{
            throw new DataNotFoundException("Worker with id of "+id+" not found!");
        }
    }

    @Override
    public boolean HardDeleteWorker(Long id) {
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
    public boolean SoftDeleteWorker(Long id) {
        Optional<Worker> selectedWorker = workerRepository.findById(id);
        if(selectedWorker.isPresent())
        {
            selectedWorker.get().setDeleted(true);
            workerRepository.save(selectedWorker.get());
            return true;
        }
        else{
            throw new DataNotFoundException("Worker with id of "+ id+" is not found!");
        }
    }
    @Override
    public List<WorkerGetResponse> GetWorkers() {
        List<Worker> workers = workerRepository.findAll();
        List<WorkerGetResponse> workerGetResponses = new ArrayList<>();
        for(Worker worker : workers)
        {
            WorkerGetResponse workerGetResponse = WorkerMapper.INSTANCE.workerToWorkerGetResponse(worker);
            workerGetResponses.add(workerGetResponse);
        }
        return workerGetResponses;
    }

    @Override
    public WorkerGetResponse GetWorkerById(Long id) {
        Optional<Worker> worker = workerRepository.findById(id);
        if(worker.isPresent())
        {
            return WorkerMapper.INSTANCE.workerToWorkerGetResponse(worker.get());
        }
        else{
            throw new DataNotFoundException("Worker with id of " + id + " is not found!");
        }
    }

    @Override
    public List<WorkerGetResponse> GetWorkersByOrganizationId(Long id) {
        List<Worker> workers = workerRepository.findByOrganizationId(id);
        List<WorkerGetResponse> workerGetResponses = new ArrayList<>();
        for(Worker worker : workers)
        {
            WorkerGetResponse workerGetResponse = WorkerMapper.INSTANCE.workerToWorkerGetResponse(worker);
            workerGetResponses.add(workerGetResponse);
        }
        return workerGetResponses;
    }
}
