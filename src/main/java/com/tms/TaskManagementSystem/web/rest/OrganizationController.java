package com.tms.TaskManagementSystem.web.rest;

import com.tms.TaskManagementSystem.request.Organization.CreateOrganizationRequest;
import com.tms.TaskManagementSystem.request.Organization.UpdateOrganizationRequest;
import com.tms.TaskManagementSystem.response.Organization.OrganizationGetAllResponse;
import com.tms.TaskManagementSystem.response.Organization.OrganizationGetResponse;
import com.tms.TaskManagementSystem.response.Organization.OrganizationResponse;
import com.tms.TaskManagementSystem.services.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/organization")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService organizationService;

    @PostMapping("/addOrganization")
    @ResponseStatus(HttpStatus.CREATED)
    public OrganizationResponse addOrganization(@RequestBody CreateOrganizationRequest request)
    {
        return organizationService.AddOrganization(request);
    }

    @GetMapping("/getOrganization/{id}")
    public OrganizationGetResponse getOrganization(@PathVariable Long id)
    {
            return organizationService.GetOrganizationById(id);
    }

    @GetMapping("/getOrganizations")
    public List<OrganizationGetAllResponse> getOrganizations()
    {
        return organizationService.GetOrganizations();
    }

    @GetMapping("/getOrganizationsWithWorkers")
    public List<OrganizationGetResponse> getOrganizationsWithWorkers()
    {
        return organizationService.GetOrganizationsWithWorkers();
    }

    @PutMapping("/updateOrganization/{id}")
    public  OrganizationResponse updateOrganization(@PathVariable Long id, @RequestBody UpdateOrganizationRequest request)
    {
            return organizationService.UpdateOrganization(id, request);
    }

    @DeleteMapping("/softDeleteOrganization/{id}")
    public boolean softDeleteOrganization(@PathVariable Long id)
    {
            return organizationService.SoftDeleteOrganization(id);
    }

    @DeleteMapping("/hardDeleteOrganization/{id}")
    public boolean hardDeleteOrganization(@PathVariable Long id)
    {
            return organizationService.HardDeleteOrganization(id);
    }
}