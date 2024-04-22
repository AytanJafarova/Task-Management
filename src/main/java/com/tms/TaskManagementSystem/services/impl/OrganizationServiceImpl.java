package com.tms.TaskManagementSystem.services.impl;

import com.tms.TaskManagementSystem.dto.WorkerDTO;
import com.tms.TaskManagementSystem.entity.Organization;
import com.tms.TaskManagementSystem.entity.Worker;
import com.tms.TaskManagementSystem.entity.enums.OrganizationStatus;
import com.tms.TaskManagementSystem.exception.DataNotFoundException;
import com.tms.TaskManagementSystem.exception.IllegalArgumentException;
import com.tms.TaskManagementSystem.exception.response.ResponseMessage;
import com.tms.TaskManagementSystem.mappers.OrganizationMapper;
import com.tms.TaskManagementSystem.repository.OrganizationRepository;
import com.tms.TaskManagementSystem.request.Organization.CreateOrganizationRequest;
import com.tms.TaskManagementSystem.request.Organization.UpdateOrganizationRequest;
import com.tms.TaskManagementSystem.response.Organization.OrganizationListResponse;
import com.tms.TaskManagementSystem.response.Organization.OrganizationResponse;
import com.tms.TaskManagementSystem.response.Organization.OrganizationWorkersResponse;
import com.tms.TaskManagementSystem.services.OrganizationService;

import com.tms.TaskManagementSystem.utils.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository organizationRepository;
    boolean checkingName(String name)
    {
        if (name.isBlank()) {
            throw new IllegalArgumentException(ResponseMessage.ERROR_INVALID_ORGANIZATION_NAME);
        }
        organizationRepository.findByName(name.toLowerCase())
                .ifPresent(org -> {
                    throw new IllegalArgumentException(ResponseMessage.ERROR_ORGANIZATION_EXISTS);
                });
        return true;
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
            throw new IllegalArgumentException(ResponseMessage.ERROR_INVALID_OPERATION);
        }
    }

    @Override
    public OrganizationResponse update(Long id,UpdateOrganizationRequest request) {

        Organization organization = organizationRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException(ResponseMessage.ERROR_ORGANIZATION_NOT_FOUND_BY_ID));
        if(checkingName(request.getName()))
        {
            organization.setName(request.getName());
            organizationRepository.save(organization);
            return OrganizationMapper.INSTANCE.organizationToOrganizationResponse(organization);
        }
        else{
            throw new IllegalArgumentException(ResponseMessage.ERROR_INVALID_OPERATION);
        }
    }

    @Override
    public boolean inactivate(Long id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException(ResponseMessage.ERROR_ORGANIZATION_NOT_FOUND_BY_ID));
        organization.setStatus(OrganizationStatus.INACTIVE);
        organizationRepository.save(organization);
        return true;
    }

    @Override
    public boolean delete(Long id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException(ResponseMessage.ERROR_ORGANIZATION_NOT_FOUND_BY_ID));
        organizationRepository.delete(organization);
        return true;
    }

    @Override
    public OrganizationListResponse getOrganizations(Pageable pageable) {
        Page<Organization> organizations = organizationRepository.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        OrganizationListResponse response = OrganizationListResponse.builder().build();
        response.setItems(organizations.getContent().stream().map(OrganizationMapper.INSTANCE::organizationToOrganizationResponse).collect(Collectors.toList()));
        response.setPaginationInfo(PaginationUtil.getPaginationInfo(organizations));
        return response;
    }

    @Override
    public OrganizationListResponse getActiveOrganizations(Pageable pageable) {
        Page<Organization> organizations = organizationRepository.findByStatus(OrganizationStatus.ACTIVE, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        OrganizationListResponse response = OrganizationListResponse.builder().build();
        response.setItems(organizations.getContent().stream().map(OrganizationMapper.INSTANCE::organizationToOrganizationResponse).collect(Collectors.toList()));
        response.setPaginationInfo(PaginationUtil.getPaginationInfo(organizations));
        return response;
    }

    @Override
    public List<OrganizationWorkersResponse> getOrganizationsWorkers(Pageable pageable) {
        Page<Organization> organizations = organizationRepository.findAllWithWorkers(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        List<OrganizationWorkersResponse> organizationResponses = new ArrayList<>();
        for (Organization organization : organizations) {
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
            OrganizationWorkersResponse organizationResponse = OrganizationWorkersResponse.builder()
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
    public OrganizationWorkersResponse getOrganizationById(Long id) {
        Organization organization  = organizationRepository.findByIdAndStatus(id,OrganizationStatus.ACTIVE)
                .orElseThrow(()-> new DataNotFoundException(ResponseMessage.ERROR_ORGANIZATION_NOT_FOUND_BY_ID));

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
                        .status(worker.getStatus())
                        .build();
                workerDTOs.add(workerDTO);
            }
            return OrganizationWorkersResponse.builder()
                    .name(organization.getName())
                    .id(organization.getId())
                    .status(organization.getStatus())
                    .workers(workerDTOs)
                    .build();
    }
}
