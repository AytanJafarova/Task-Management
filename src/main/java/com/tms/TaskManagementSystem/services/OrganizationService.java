package com.tms.TaskManagementSystem.services;

import com.tms.TaskManagementSystem.request.Organization.CreateOrganizationRequest;
import com.tms.TaskManagementSystem.request.Organization.UpdateOrganizationRequest;
import com.tms.TaskManagementSystem.response.Organization.OrganizationListResponse;
import com.tms.TaskManagementSystem.response.Organization.OrganizationResponse;
import com.tms.TaskManagementSystem.response.Organization.OrganizationWorkersResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrganizationService {
    OrganizationResponse save(CreateOrganizationRequest request);
    OrganizationResponse update(Long id,UpdateOrganizationRequest request);
    boolean inactivate(Long id);
    boolean delete(Long id);
    OrganizationListResponse getOrganizations(Pageable pageable);
    OrganizationListResponse getActiveOrganizations(Pageable pageable);
    List<OrganizationWorkersResponse> getOrganizationsWorkers(Pageable pageable);
    OrganizationWorkersResponse getOrganizationById(Long id);
}