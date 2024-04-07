package com.tms.TaskManagementSystem.services;

import com.tms.TaskManagementSystem.request.Organization.CreateOrganizationRequest;
import com.tms.TaskManagementSystem.request.Organization.UpdateOrganizationRequest;
import com.tms.TaskManagementSystem.response.Organization.OrganizationResponse;
import com.tms.TaskManagementSystem.response.Organization.OrganizationWorkersResponse;

import java.util.List;

public interface OrganizationService {
    OrganizationResponse save(CreateOrganizationRequest request);
    OrganizationResponse update(Long id,UpdateOrganizationRequest request);
    boolean inactivate(Long id);
    boolean delete(Long id);
    List<OrganizationResponse> getOrganizations(int pgNum,int pgSize);
    List<OrganizationResponse> getActiveOrganizations(int pgNum,int pgSize);
    List<OrganizationWorkersResponse> getOrganizationsWorkers(int pgNum,int pgSize);
    OrganizationWorkersResponse getOrganizationById(Long id);
}