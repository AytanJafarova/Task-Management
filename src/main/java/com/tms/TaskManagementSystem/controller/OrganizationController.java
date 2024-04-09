package com.tms.TaskManagementSystem.controller;

import com.tms.TaskManagementSystem.request.Organization.CreateOrganizationRequest;
import com.tms.TaskManagementSystem.request.Organization.UpdateOrganizationRequest;
import com.tms.TaskManagementSystem.response.Organization.OrganizationResponse;
import com.tms.TaskManagementSystem.response.Organization.OrganizationWorkersResponse;
import com.tms.TaskManagementSystem.services.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/organizations")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService organizationService;

    @PostMapping("/save")
    @Operation(summary = "Creating new Organization")
    public ResponseEntity<OrganizationResponse> save(@RequestBody CreateOrganizationRequest request)
    {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(organizationService.save(request));
    }

    @PutMapping("update/{id}")
    @Operation(summary = "Updating Organization by organization id")
    public  ResponseEntity<OrganizationResponse> update(@PathVariable Long id, @RequestBody UpdateOrganizationRequest request)
    {
        return ResponseEntity.ok(organizationService.update(id, request));
    }

    @PutMapping("/inactivate/{id}")
    @Operation(summary = "Inactivate Organization by organization id")
    public ResponseEntity<Boolean> inactivate(@PathVariable Long id)
    {
        return ResponseEntity.ok(organizationService.inactivate(id));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete Organization by organization id")
    public ResponseEntity<Boolean> delete(@PathVariable Long id)
    {
        return ResponseEntity.ok(organizationService.delete(id));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find Organization by organization id")
    public ResponseEntity<OrganizationWorkersResponse> getOrganization(@PathVariable Long id)
    {
            return ResponseEntity.ok(organizationService.getOrganizationById(id));
    }

    @GetMapping("/all")
    @Operation(summary = "Find all Organizations")
    public ResponseEntity<List<OrganizationResponse>> getOrganizations(Pageable pageable)
    {
        return ResponseEntity.ok(organizationService.getOrganizations(pageable));
    }

    @GetMapping("")
    @Operation(summary = "Find all active Organizations")
    public ResponseEntity<List<OrganizationResponse>> getActiveOrganizations(Pageable pageable)
    {
        return ResponseEntity.ok(organizationService.getActiveOrganizations(pageable));
    }

    @GetMapping("/workers")
    @Operation(summary = "Find Organizations with Workers")
    public ResponseEntity<List<OrganizationWorkersResponse>> getOrganizationsWorkers(Pageable pageable)
    {
        return ResponseEntity.ok(organizationService.getOrganizationsWorkers(pageable));
    }
}