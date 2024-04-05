package com.tms.TaskManagementSystem.services.impl;

import com.tms.TaskManagementSystem.domain.Organization;
import com.tms.TaskManagementSystem.domain.Worker;
import com.tms.TaskManagementSystem.exception.DataNotFoundException;
import com.tms.TaskManagementSystem.exception.IllegalArgumentException;
import com.tms.TaskManagementSystem.mappers.OrganizationMapper;
import com.tms.TaskManagementSystem.repository.OrganizationRepository;
import com.tms.TaskManagementSystem.request.Organization.CreateOrganizationRequest;
import com.tms.TaskManagementSystem.request.Organization.UpdateOrganizationRequest;
import com.tms.TaskManagementSystem.response.Organization.OrganizationGetAllResponse;
import com.tms.TaskManagementSystem.response.Organization.OrganizationGetResponse;
import com.tms.TaskManagementSystem.response.Organization.OrganizationResponse;
import com.tms.TaskManagementSystem.response.Worker.WorkerForOrganizationResponse;
import com.tms.TaskManagementSystem.services.OrganizationService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository organizationRepository;
    boolean checkingName(String name)
    {
        List<Organization> organizations = organizationRepository.findAll();
        boolean adding = true;
        if(name !=null && !name.isBlank())
        {
            for(Organization organization : organizations)
            {
                if (organization.getName().equalsIgnoreCase(name))
                {
                    adding = false;
                    break;
                }
            }
        }
        else{
            adding=false;
        }
        return adding;
    }

    @Override
    public OrganizationResponse AddOrganization(CreateOrganizationRequest request) {
        if(checkingName(request.getName()))
        {
            Organization organization = organizationRepository.save(Organization.builder()
                    .name(request.getName()).build());
            return OrganizationResponse.builder().name(organization.getName()).build();
        }
        else{
            throw new IllegalArgumentException("Invalid operation!");
        }
    }

    @Override
    public OrganizationResponse UpdateOrganization(Long id,UpdateOrganizationRequest request) {

        Optional<Organization> selectedOrganization = organizationRepository.findById(id);
        if(selectedOrganization.isPresent())
        {
            if(checkingName(request.getName()))
            {
                selectedOrganization.get().setName(request.getName());
                organizationRepository.save(selectedOrganization.get());
                return OrganizationMapper.INSTANCE.organizationToOrganizationResponse(selectedOrganization.get());
            }
            else{
                throw new IllegalArgumentException("Invalid operation!");
            }
        }
        throw new DataNotFoundException("Organization with id of" + id + "is not found!");
    }

    @Override
    public boolean SoftDeleteOrganization(Long id) {
        Optional<Organization> selectedOrganization = organizationRepository.findById(id);
        if(selectedOrganization.isPresent())
        {
            selectedOrganization.get().setDeleted(true);
            organizationRepository.save(selectedOrganization.get());
            return true;
        }
        else{
            throw new DataNotFoundException("Organization with id of "+id+" is not found!");
        }
    }

    @Override
    public boolean HardDeleteOrganization(Long id) {
        Optional<Organization> selectedOrganization = organizationRepository.findById(id);
        if(selectedOrganization.isPresent())
        {
            organizationRepository.delete(selectedOrganization.get());
            return true;
        }
        else{
            throw new DataNotFoundException("Organization with id of "+ id+" is not found!");
        }
    }

    @Override
    public List<OrganizationGetAllResponse> GetOrganizations() {
        List<Organization> organizations = organizationRepository.findAll();
        List<OrganizationGetAllResponse> organizationGetResponses = new ArrayList<>();
        for(Organization organization : organizations)
        {
                OrganizationGetAllResponse organizationGetResponse = OrganizationMapper.INSTANCE.organizationToOrganizationGetAllResponse(organization);
                organizationGetResponses.add(organizationGetResponse);
        }
        return organizationGetResponses;
    }

    @Override
    public List<OrganizationGetResponse> GetOrganizationsWithWorkers() {
        List<Organization> organizations = organizationRepository.findAllWithWorkers();
        List<OrganizationGetResponse> organizationGetResponses = new ArrayList<>();
        for (Organization organization : organizations) {
            List<Worker> workers = organization.getWorkers();
            List<WorkerForOrganizationResponse> workerResponses = new ArrayList<>();
            for (Worker worker : workers) {
                WorkerForOrganizationResponse workerResponse = WorkerForOrganizationResponse.builder()
                        .username(worker.getUsername())
                        .password(worker.getPassword())
                        .email(worker.getEmail())
                        .build();
                workerResponses.add(workerResponse);
            }
            OrganizationGetResponse organizationGetResponse = OrganizationGetResponse.builder()
                    .name(organization.getName())
                    .worker(workerResponses)
                    .build();

            organizationGetResponses.add(organizationGetResponse);
        }
        return organizationGetResponses;
    }

    @Override
    public OrganizationGetResponse GetOrganizationById(Long id) {
        Optional<Organization> organizationOptional = organizationRepository.findById(id);
        if (organizationOptional.isPresent()) {
            Organization organization = organizationOptional.get();
            List<Worker> workers = organization.getWorkers();
            List<WorkerForOrganizationResponse> workerResponses = new ArrayList<>();
            for (Worker worker : workers) {
                WorkerForOrganizationResponse workerResponse = WorkerForOrganizationResponse.builder()
                        .username(worker.getUsername())
                        .password(worker.getPassword())
                        .email(worker.getEmail())
                        .build();
                workerResponses.add(workerResponse);
            }
            return OrganizationGetResponse.builder()
                    .name(organization.getName())
                    .worker(workerResponses)
                    .build();
        } else {
            throw new DataNotFoundException("Organization with id of " + id + " is not found!");
        }
    }
}
