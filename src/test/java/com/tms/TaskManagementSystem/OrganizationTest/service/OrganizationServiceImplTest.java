package com.tms.TaskManagementSystem.OrganizationTest.service;

import com.tms.TaskManagementSystem.entity.Worker;
import com.tms.TaskManagementSystem.entity.enums.OrganizationStatus;
import com.tms.TaskManagementSystem.repository.OrganizationRepository;
import com.tms.TaskManagementSystem.response.Organization.OrganizationResponse;
import com.tms.TaskManagementSystem.services.impl.OrganizationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceImplTest {
    @Mock
    OrganizationRepository organizationRepository;

    @InjectMocks
    OrganizationServiceImpl organizationService;

    private final Long id = 1L;
    private final String organizationName = "test_organization";
    private final OrganizationStatus statusActive = OrganizationStatus.ACTIVE;
    private final List<Worker> workers = new ArrayList<>();
    private final OrganizationResponse organization= new OrganizationResponse();

    @BeforeEach
    void setUp() {
        organization.setId(id);
        organization.setName(organizationName);
        organization.setStatus(statusActive);
    }
}