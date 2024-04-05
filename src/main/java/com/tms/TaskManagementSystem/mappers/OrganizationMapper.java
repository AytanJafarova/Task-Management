package com.tms.TaskManagementSystem.mappers;

import com.tms.TaskManagementSystem.domain.Organization;
import com.tms.TaskManagementSystem.request.Organization.CreateOrganizationRequest;
import com.tms.TaskManagementSystem.request.Organization.UpdateOrganizationRequest;
import com.tms.TaskManagementSystem.response.Organization.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {
    OrganizationMapper INSTANCE = Mappers.getMapper(OrganizationMapper.class);
    OrganizationResponse organizationToOrganizationResponse(Organization organization);
    OrganizationGetResponse organizationToOrganizationGetResponse(Organization organization);
    OrganizationForWorkerResponse organizationToOrganizationForWorkerResponse(Organization organization);
    OrganizationGetAllResponse organizationToOrganizationGetAllResponse(Organization organization);
    Organization createOrganizationRequestToOrganization(CreateOrganizationRequest organizationRequest);
    Organization updateOrganizationRequestToOrganization(UpdateOrganizationRequest organizationRequest);
}