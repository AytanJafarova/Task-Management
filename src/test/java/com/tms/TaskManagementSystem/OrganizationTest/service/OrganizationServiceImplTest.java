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
import com.tms.TaskManagementSystem.response.Organization.OrganizationWorkersResponse;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    void testCheckingNameInvalidShouldThrowIllegalArgumentException() {
        // Given
        when(organizationService.checkingName(organizationName, ignoreName)).thenThrow(new IllegalArgumentException("Invalid organization name"));

        // Then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> organizationServiceImpl.checkingName(organizationName, true));
        verify(organizationService, times(1)).checkingName(organizationName, true);
        verifyNoMoreInteractions(organizationService);
    }

    @Test
    void testCheckingNameExistingShouldThrowIllegalArgumentException()
    {
        // Given
        when(organizationService.checkingName(organizationName,!ignoreName)).thenThrow(new IllegalArgumentException("Organization with the specified name already exists"));

       // Then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(()->organizationService.checkingName(organizationName,false));
        verify(organizationService, times(1)).checkingName(organizationName,false);
        verifyNoMoreInteractions(organizationService);
    }

    @Test
    void testCheckingNameValidShouldReturnTrue()
    {
        // Given
        when(organizationService.checkingName(organizationName,ignoreName)).thenReturn(true);

        // When
        Boolean checkingResult = organizationService.checkingName(organizationName,true);

        // Then
        assertThat(checkingResult).isTrue();
        verify(organizationService, times(1)).checkingName(organizationName,true);
        verifyNoMoreInteractions(organizationService);
    }

    @Test
    void testSaveOrganizationShouldReturnOrganization() {
        // Given
        CreateOrganizationRequest request = CreateOrganizationRequest.builder()
                .name(organizationName).build();

        Organization savedOrganization = new Organization();
        savedOrganization.setId(id);
        savedOrganization.setStatus(statusActive);
        savedOrganization.setWorkers(workers);
        savedOrganization.setName(organizationName);
        when(organizationRepository.save(any(Organization.class))).thenReturn(savedOrganization);

        // When
        OrganizationResponse response = organizationServiceImpl.save(request);

        // Then
        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("test_organization", response.getName());
    }

    @Test
    void testSaveOrganizationShouldThrowIllegalArgumentException() {
        // Given
        when(organizationRepository.save(Organization.builder().name(organizationName).id(id).status(statusActive).workers(workers).build())).thenThrow(new IllegalArgumentException("Invalid operation!"));

        // Then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(()->organizationServiceImpl.save(CreateOrganizationRequest.builder().name("test_organization").build()));
        verify(organizationRepository, times(1)).save(Organization.builder().name(organizationName).id(id).status(statusActive).workers(workers).build());
        verifyNoMoreInteractions(organizationRepository);
    }

    @Test
    void testUpdateOrganizationShouldReturnOrganization() {
        // Given
        UpdateOrganizationRequest request = UpdateOrganizationRequest.builder().name(organizationName).build();

        Organization updatedOrganization = new Organization();
        updatedOrganization.setId(id);
        updatedOrganization.setStatus(statusActive);
        updatedOrganization.setWorkers(workers);
        updatedOrganization.setName(organizationName);
        when(organizationRepository.save(any(Organization.class))).thenReturn(updatedOrganization);

        // When
        OrganizationResponse response = organizationServiceImpl.update(10L,request);

        // Then
        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("test_organization", response.getName());
    }

    @Test
    void testUpdateOrganizationShouldThrowIllegalArgumentException() {
        // Given
        when(organizationService.update(id,UpdateOrganizationRequest.builder().name(organizationName).build())).thenThrow(new IllegalArgumentException("Invalid operation!"));

        // Then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(()->organizationService.update(10L,UpdateOrganizationRequest.builder().name("test_organization").build()));
        verify(organizationService, times(1)).update(id,UpdateOrganizationRequest.builder().name(organizationName).build());
        verifyNoMoreInteractions(organizationService);
    }

    @Test
    void testInactiveOrganizationShouldReturnTrue() {
        // Given
        when(organizationService.inactivate(id)).thenReturn(inactivate);

        // Then
        assertThat(organizationService.inactivate(10L)).isEqualTo(inactivate);
        verify(organizationService, times(1)).inactivate(id);
        verifyNoMoreInteractions(organizationService);
    }

    @Test
    void testInactiveOrganizationShouldThrowDataNotFoundException() {
        // Given
        when(organizationService.inactivate(id)).thenThrow(new DataNotFoundException("Organization not found by given id"));

        // Then
        assertThatExceptionOfType(DataNotFoundException.class).isThrownBy(()->organizationService.inactivate(10L));
        verify(organizationService, times(1)).inactivate(id);
        verifyNoMoreInteractions(organizationService);
    }

    @Test
    void testDeleteShouldReturnTrue() {
        // Given
        Organization existingOrganization = new Organization();
        existingOrganization.setId(id);
        existingOrganization.setName(organizationName);
        existingOrganization.setStatus(statusActive);
        existingOrganization.setWorkers(workers);
        when(organizationRepository.findByIdAndStatus(id,statusActive)).thenReturn(Optional.of(existingOrganization));

        // When
        Boolean result = organizationServiceImpl.delete(10L);

        // Then
        assertThat(result).isTrue();
        verify(organizationRepository,times(1)).delete(existingOrganization);
        verifyNoMoreInteractions();
    }

    @Test
    void testDeleteShouldThrowIllegalArgumentException() {
        // Given
        when(organizationRepository.findByIdAndStatus(id,statusActive)).thenThrow(new DataNotFoundException("Organization not found by given id"));

        // Then
        assertThatExceptionOfType(DataNotFoundException.class).isThrownBy(()->organizationServiceImpl.delete(10L));
    }

    @Test
    void testGetOrganizationByIdShouldReturnOrganization() {
        // Given
        Organization existingOrganization = new Organization();
        existingOrganization.setId(id);
        existingOrganization.setName(organizationName);
        existingOrganization.setStatus(statusActive);
        existingOrganization.setWorkers(workers);
        when(organizationRepository.findByIdAndStatus(id,statusActive)).thenReturn(Optional.of(existingOrganization));

        // When
        OrganizationWorkersResponse response = organizationServiceImpl.getOrganizationById(10L);

        // Then
        assertNotNull(response);
    }

    @Test
    void testGetOrganizationByIdShouldThrowDataNotFoundException()
    {
        // When
        when(organizationRepository.findByIdAndStatus(id,statusActive)).thenThrow(new DataNotFoundException("Organization not found by given id"));

       // Then
        assertThatExceptionOfType(DataNotFoundException.class).isThrownBy(()->organizationServiceImpl.getOrganizationById(10L));
    }
}