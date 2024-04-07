package com.tms.TaskManagementSystem.controller;

import com.tms.TaskManagementSystem.request.Organization.CreateOrganizationRequest;
import com.tms.TaskManagementSystem.request.Organization.UpdateOrganizationRequest;
import com.tms.TaskManagementSystem.response.Organization.OrganizationResponse;
import com.tms.TaskManagementSystem.response.Organization.OrganizationWorkersResponse;
import com.tms.TaskManagementSystem.services.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/organizations")
@RequiredArgsConstructor
//@Api("Organization Operations")

public class OrganizationController {
    private final OrganizationService organizationService;

    @PostMapping("/save")
//    @ApiOperation(value = "Creating new Organization")
    @ResponseStatus(HttpStatus.CREATED)
    public OrganizationResponse save(@RequestBody CreateOrganizationRequest request)
    {
        return organizationService.save(request);
    }

    @PutMapping("update/{id}")
//    @ApiOperation(value = "Updating Organization by organization id")
    public  OrganizationResponse update(@PathVariable Long id, @RequestBody UpdateOrganizationRequest request)
    {
        return organizationService.update(id, request);
    }

    @PutMapping("/inactivate/{id}")
//    @ApiOperation(value = "Inactivate Organization by organization id")
    public boolean inactivate(@PathVariable Long id)
    {
        return organizationService.inactivate(id);
    }

    @DeleteMapping("/delete/{id}")
//    @ApiOperation(value = "Delete Organization by organization id")
    public ResponseEntity<Boolean> delete(@PathVariable Long id)
    {
        return ResponseEntity.ok(organizationService.delete(id));
    }

    @GetMapping("/{id}")
//    @ApiOperation(value = "Find Organization by organization id")
    public ResponseEntity<OrganizationWorkersResponse> getOrganization(@PathVariable Long id)
    {
            return ResponseEntity.ok(organizationService.getOrganizationById(id));
    }

    @GetMapping("/all")
//    @ApiOperation(value = "Find all Organizations")
    public ResponseEntity<List<OrganizationResponse>> getOrganizations(@RequestParam(defaultValue = "0") int pageNumber,@RequestParam(defaultValue = "10") int pageSize)
    {
        return ResponseEntity.ok(organizationService.getOrganizations(pageNumber,pageSize));
    }

    @GetMapping("")
//    @ApiOperation(value = "Find all active Organizations")
    public ResponseEntity<List<OrganizationResponse>> getActiveOrganizations(@RequestParam(defaultValue = "0") int pageNumber,@RequestParam(defaultValue = "10") int pageSize)
    {
        return ResponseEntity.ok(organizationService.getActiveOrganizations(pageNumber,pageSize));
    }

    @GetMapping("/workers")
//    @ApiOperation(value = "Find Organizations with Workers")
    public ResponseEntity<List<OrganizationWorkersResponse>> getOrganizationsWorkers(@RequestParam(defaultValue = "0") int pageNumber,@RequestParam(defaultValue = "10") int pageSize)
    {
        return ResponseEntity.ok(organizationService.getOrganizationsWorkers(pageNumber,pageSize));
    }
}