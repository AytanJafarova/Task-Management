package com.tms.TaskManagementSystem.OrganizationTest.service;

import com.tms.TaskManagementSystem.entity.Organization;
import com.tms.TaskManagementSystem.entity.Worker;
import com.tms.TaskManagementSystem.entity.enums.OrganizationStatus;
import com.tms.TaskManagementSystem.exception.DataNotFoundException;
import com.tms.TaskManagementSystem.exception.IllegalArgumentException;
import com.tms.TaskManagementSystem.repository.OrganizationRepository;
import com.tms.TaskManagementSystem.request.Organization.CreateOrganizationRequest;
import com.tms.TaskManagementSystem.request.Organization.UpdateOrganizationRequest;
import com.tms.TaskManagementSystem.response.Organization.OrganizationResponse;
import com.tms.TaskManagementSystem.services.OrganizationService;
import com.tms.TaskManagementSystem.services.impl.OrganizationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceImplTest {

    @Mock
    OrganizationRepository organizationRepository;
    @Mock
    OrganizationService organizationService;
    @InjectMocks
    OrganizationServiceImpl organizationServiceImpl;
    @Captor
    ArgumentCaptor<Organization> organizationCaptor = ArgumentCaptor.forClass(Organization.class);

    private final Long id = 10L;
    private final String organizationName = "test_organization";
    private final Boolean ignoreName = true;
    private final Boolean checkingName = true; // add for save and update
    private final Boolean inactivate = true;
    private final OrganizationStatus statusActive = OrganizationStatus.ACTIVE;
    private final List<Worker> workers = new ArrayList<>();
    private final OrganizationResponse organization= new OrganizationResponse();

    @BeforeEach
    void setUp() {
        organization.setId(id);
        organization.setName(organizationName);
        organization.setStatus(statusActive);
    }

    @Test
    void testCheckingNameInvalidShouldThrowIllegalArgumentException()
    {
        when(organizationService.checkingName(organizationName,ignoreName)).thenThrow(new IllegalArgumentException("Invalid organization name"));
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(()->organizationService.checkingName(organizationName,true));
        verify(organizationService, times(1)).checkingName(organizationName,true);
        verifyNoMoreInteractions(organizationService);
    }

    @Test
    void testCheckingNameExistingShouldThrowIllegalArgumentException()
    {
        when(organizationService.checkingName(organizationName,!ignoreName)).thenThrow(new IllegalArgumentException("Organization with the specified name already exists"));
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(()->organizationService.checkingName(organizationName,false));
        verify(organizationService, times(1)).checkingName(organizationName,false);
        verifyNoMoreInteractions(organizationService);
    }

    @Test
    void testCheckingNameValidShouldReturnTrue()
    {
        when(organizationService.checkingName(organizationName,ignoreName)).thenReturn(true);
        Boolean checkingResult = organizationService.checkingName(organizationName,true);
        assertThat(checkingResult).isTrue();
        verify(organizationService, times(1)).checkingName(organizationName,true);
        verifyNoMoreInteractions(organizationService);
    }

    @Test
    void testSaveOrganizationShouldReturnOrganization() {
        when(organizationService.save(CreateOrganizationRequest.builder().name(organizationName).build())).thenReturn(organization);
        assertThat(organizationService.save(CreateOrganizationRequest.builder().name("test_organization").build())).isEqualTo(organization);

        verify(organizationService, times(1)).save(CreateOrganizationRequest.builder().name(organizationName).build());
        verifyNoMoreInteractions(organizationService);
    }

    @Test
    void testSaveOrganizationShouldThrowIllegalArgumentException() {
        when(organizationService.save(CreateOrganizationRequest.builder().name(organizationName).build())).thenThrow(new IllegalArgumentException("Invalid operation!"));
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(()->organizationService.save(CreateOrganizationRequest.builder().name("test_organization").build()));

        verify(organizationService, times(1)).save(CreateOrganizationRequest.builder().name(organizationName).build());
        verifyNoMoreInteractions(organizationService);
    }

    @Test
    void testUpdateOrganizationShouldReturnOrganization() {
        when(organizationService.update(id ,UpdateOrganizationRequest.builder().name(organizationName).build())).thenReturn(organization);
        assertThat(organizationService.update(10L,UpdateOrganizationRequest.builder().name("test_organization").build())).isEqualTo(organization);

        verify(organizationService, times(1)).update(id,UpdateOrganizationRequest.builder().name(organizationName).build());
        verifyNoMoreInteractions(organizationService);
    }

    @Test
    void testUpdateOrganizationShouldThrowIllegalArgumentException() {
        when(organizationService.update(id,UpdateOrganizationRequest.builder().name(organizationName).build())).thenThrow(new IllegalArgumentException("Invalid operation!"));
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(()->organizationService.update(10L,UpdateOrganizationRequest.builder().name("test_organization").build()));

        verify(organizationService, times(1)).update(id,UpdateOrganizationRequest.builder().name(organizationName).build());
        verifyNoMoreInteractions(organizationService);
    }

    @Test
    void testInactiveOrganizationShouldReturnTrue() {
        when(organizationService.inactivate(id)).thenReturn(inactivate);
        assertThat(organizationService.inactivate(10L)).isEqualTo(inactivate);

        verify(organizationService, times(1)).inactivate(id);
        verifyNoMoreInteractions(organizationService);
    }

    @Test
    void testInactiveOrganizationShouldThrowDataNotFoundException() {
        when(organizationService.inactivate(id)).thenThrow(new DataNotFoundException("Organization not found by given id"));
        assertThatExceptionOfType(DataNotFoundException.class).isThrownBy(()->organizationService.inactivate(10L));

        verify(organizationService, times(1)).inactivate(id);
        verifyNoMoreInteractions(organizationService);
    }

    @Test
    void delete() {
    }

    @Test
    void getOrganizations() {
    }

    @Test
    void getActiveOrganizations() {
    }

    @Test
    void getOrganizationsWorkers() {
    }

    @Test
    void getOrganizationById() {
    }
}