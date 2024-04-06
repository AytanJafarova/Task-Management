package com.tms.TaskManagementSystem.mappers;

import com.tms.TaskManagementSystem.entity.Organization;
import com.tms.TaskManagementSystem.dto.OrganizationDTO;
import com.tms.TaskManagementSystem.request.Organization.CreateOrganizationRequest;
import com.tms.TaskManagementSystem.request.Organization.UpdateOrganizationRequest;
import com.tms.TaskManagementSystem.response.Organization.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {
    OrganizationMapper INSTANCE = Mappers.getMapper(OrganizationMapper.class);
    OrganizationResponse organizationToOrganizationResponse(Organization organization);
    OrganizationWorkersResponse organizationToOrganizationWorkerResponse(Organization organization);
    OrganizationDTO organizationToOrganizationDTO(Organization organization);
    Organization createOrganizationRequestToOrganization(CreateOrganizationRequest organizationRequest);
    Organization updateOrganizationRequestToOrganization(UpdateOrganizationRequest organizationRequest);
}