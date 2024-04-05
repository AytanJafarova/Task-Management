package com.tms.TaskManagementSystem.services;

import com.tms.TaskManagementSystem.request.Organization.CreateOrganizationRequest;
import com.tms.TaskManagementSystem.request.Organization.UpdateOrganizationRequest;
import com.tms.TaskManagementSystem.response.Organization.OrganizationGetAllResponse;
import com.tms.TaskManagementSystem.response.Organization.OrganizationGetResponse;
import com.tms.TaskManagementSystem.response.Organization.OrganizationResponse;

import java.util.List;

public interface OrganizationService {
    OrganizationResponse AddOrganization(CreateOrganizationRequest request);
    OrganizationResponse UpdateOrganization(Long id,UpdateOrganizationRequest request);
    boolean SoftDeleteOrganization(Long id);
    boolean HardDeleteOrganization(Long id);
    List<OrganizationGetAllResponse> GetOrganizations();
    List<OrganizationGetResponse> GetOrganizationsWithWorkers();
    OrganizationGetResponse GetOrganizationById(Long id);
}