package com.tms.TaskManagementSystem.services.impl;

import com.tms.TaskManagementSystem.dto.WorkerDTO;
import com.tms.TaskManagementSystem.entity.Organization;
import com.tms.TaskManagementSystem.entity.Worker;
import com.tms.TaskManagementSystem.entity.enums.OrganizationStatus;
import com.tms.TaskManagementSystem.exception.DataNotFoundException;
import com.tms.TaskManagementSystem.exception.IllegalArgumentException;
import com.tms.TaskManagementSystem.mappers.OrganizationMapper;
import com.tms.TaskManagementSystem.repository.OrganizationRepository;
import com.tms.TaskManagementSystem.request.Organization.CreateOrganizationRequest;
import com.tms.TaskManagementSystem.request.Organization.UpdateOrganizationRequest;
import com.tms.TaskManagementSystem.response.Organization.OrganizationResponse;
import com.tms.TaskManagementSystem.response.Organization.OrganizationWorkersResponse;
import com.tms.TaskManagementSystem.services.OrganizationService;

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
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository organizationRepository;
    boolean checkingName(String name)
    {
        Optional<Organization> organization = organizationRepository.findByName(name.toLowerCase());
        return !name.isBlank() && organization.isEmpty();
    }

    @Override
    public OrganizationResponse save(CreateOrganizationRequest request) {
        if(checkingName(request.getName()))
        {
            Organization organization = organizationRepository.save(Organization.builder()
                    .name(request.getName())
                    .status(OrganizationStatus.ACTIVE).build());
            return OrganizationResponse.builder().name(organization.getName())
                    .status(organization.getStatus())
                    .id(organization.getId())
                    .build();
        }
        else{
            throw new IllegalArgumentException("Invalid operation!");
        }
    }

    @Override
    public OrganizationResponse update(Long id,UpdateOrganizationRequest request) {

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
    public boolean inactivate(Long id) {
        Optional<Organization> selectedOrganization = organizationRepository.findByIdAndStatus(id, OrganizationStatus.ACTIVE);
        if(selectedOrganization.isPresent())
        {
            selectedOrganization.get().setStatus(OrganizationStatus.INACTIVE);
            organizationRepository.save(selectedOrganization.get());
            return true;
        }
        else{
            throw new DataNotFoundException("Organization with id of "+id+" is not found!");
        }
    }

    @Override
    public boolean delete(Long id) {
        Optional<Organization> selectedOrganization = organizationRepository.findById(id);
        if(selectedOrganization.isPresent())
        {
            organizationRepository.delete(selectedOrganization.get());
            return true;
        }
        else{
            throw new DataNotFoundException("Organization with id of "+id+" is not found!");
        }
    }

    @Override
    public List<OrganizationWorkersResponse> getOrganizations(int pgNum, int pgSize) {
        Pageable pageable = PageRequest.of(pgNum, pgSize);
        Page<Organization> organizationPage = organizationRepository.findAll(pageable);
        List<OrganizationWorkersResponse> organizationResponses = new ArrayList<>();
        for(Organization organization : organizationPage)
        {
            OrganizationWorkersResponse organizationResponse = OrganizationMapper.INSTANCE.organizationToOrganizationWorkerResponse(organization);
            organizationResponses.add(organizationResponse);
        }
        return organizationResponses;
    }

    @Override
    public List<OrganizationResponse> getActiveOrganizations(int pgNum, int pgSize) {
        Pageable pageable = PageRequest.of(pgNum, pgSize);
        List<Organization> organizationPage = organizationRepository.findByStatus(OrganizationStatus.ACTIVE, pageable);
        List<OrganizationResponse> organizationResponses = new ArrayList<>();
        for(Organization organization : organizationPage)
        {
            OrganizationResponse organizationResponse = OrganizationMapper.INSTANCE.organizationToOrganizationResponse(organization);
            organizationResponses.add(organizationResponse);
        }
        return organizationResponses;
    }

    @Override
    public List<OrganizationResponse> getOrganizationsWorkers(int pgNum,int pgSize) {
        Pageable pageable = PageRequest.of(pgNum, pgSize);
        List<Organization> organizationPage = organizationRepository.findAllWithWorkers(pageable);
        List<OrganizationResponse> organizationResponses = new ArrayList<>();
        for (Organization organization : organizationPage) {
            List<Worker> workers = organization.getWorkers();
            List<WorkerDTO> workerDTOs = new ArrayList<>();
            for (Worker worker : workers) {
                WorkerDTO workerdto = WorkerDTO.builder()
                        .username(worker.getUsername())
                        .password(worker.getPassword())
                        .email(worker.getEmail())
                        .name(worker.getName())
                        .surname(worker.getSurname())
                        .status(worker.getStatus())
                        .id(worker.getId())
                        .build();
                workerDTOs.add(workerdto);
            }
            OrganizationResponse organizationResponse = OrganizationResponse.builder()
                    .name(organization.getName())
                    .id(organization.getId())
                    .status(organization.getStatus())
                    .workers(workerDTOs)
                    .build();

            organizationResponses.add(organizationResponse);
        }
        return organizationResponses;
    }

    @Override
    public OrganizationResponse getOrganizationById(Long id) {
        Optional<Organization> organizationOptional = organizationRepository.findByIdAndStatus(id,OrganizationStatus.ACTIVE);
        if (organizationOptional.isPresent()) {
            Organization organization = organizationOptional.get();
            List<Worker> workers = organization.getWorkers();
            List<WorkerDTO> workerDTOs = new ArrayList<>();
            for (Worker worker : workers) {
                WorkerDTO workerDTO = WorkerDTO.builder()
                        .username(worker.getUsername())
                        .password(worker.getPassword())
                        .email(worker.getEmail())
                        .name(worker.getName())
                        .surname(worker.getSurname())
                        .id(worker.getId())
                        .build();
                workerDTOs.add(workerDTO);
            }
            return OrganizationResponse.builder()
                    .name(organization.getName())
                    .id(organization.getId())
                    .status(organization.getStatus())
                    .workers(workerDTOs)
                    .build();
        } else {
            throw new DataNotFoundException("Organization with id of " + id + " is not found!");
        }
    }
}
