package com.tms.TaskManagementSystem.OrganizationTest.service;

import com.tms.TaskManagementSystem.entity.Organization;
import com.tms.TaskManagementSystem.entity.Worker;
import com.tms.TaskManagementSystem.entity.enums.OrganizationStatus;
import com.tms.TaskManagementSystem.exception.DataNotFoundException;
import com.tms.TaskManagementSystem.exception.IllegalArgumentException;
import com.tms.TaskManagementSystem.exception.response.ResponseMessage;
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
import static org.junit.jupiter.api.Assertions.*;
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
    void testCheckingNameInvalidShouldThrowIllegalArgumentExceptionWhenNameExists() {
        // Given
        when(organizationRepository.findByName(organizationName)).thenThrow(new IllegalArgumentException("Organization with the specified name already exists"));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> organizationServiceImpl.checkingName("test_organization",anyBoolean()));

        verify(organizationRepository, times(1)).findByName("test_organization");
        verifyNoMoreInteractions(organizationRepository);
    }
    @Test
    void testCheckingNameInvalidShouldThrowIllegalArgumentExceptionWhenNameNull() {
        String blank_name = " ";
        // Given
        when(organizationRepository.findByName(organizationName)).thenThrow(new IllegalArgumentException("Invalid organization name"));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> organizationServiceImpl.checkingName(blank_name,true));

        verify(organizationRepository, times(1)).findByName(" ");
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
        when(organizationRepository.save(any(Organization.class)))
                .thenThrow(new IllegalArgumentException("Invalid operation!"));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> organizationServiceImpl.save(CreateOrganizationRequest.builder().name(organizationName).build()));

        verify(organizationRepository, times(1))
                .save(any(Organization.class));
    }

    @Test
    void testUpdateOrganizationShouldReturnOrganization() {
        UpdateOrganizationRequest request = UpdateOrganizationRequest.builder()
                .name(organizationName).build();
        Organization savedOrganization = Organization.builder().id(id).workers(workers).status(statusActive).name(organizationName).build();

        when(organizationRepository.findByIdAndStatus(id, OrganizationStatus.ACTIVE))
                .thenReturn(Optional.of(savedOrganization));
        when(organizationRepository.save(savedOrganization)).thenReturn(savedOrganization);

        OrganizationResponse response = organizationServiceImpl.update(10L,request);

        assertNotNull(response);
        verify(organizationRepository).findByIdAndStatus(10L, statusActive);
        verify(organizationRepository).save(savedOrganization);
    }

    @Test
    void testUpdateOrganizationThrowsIllegalArgumentException() {
        String updatedName = "test_update_organization";
        UpdateOrganizationRequest request = UpdateOrganizationRequest.builder()
                .name(updatedName)
                .build();

        Organization existingOrganization = new Organization();
        existingOrganization.setId(id);
        existingOrganization.setName(organizationName);
        existingOrganization.setStatus(statusActive);
        existingOrganization.setWorkers(workers);

        when(organizationRepository.findByIdAndStatus(id,OrganizationStatus.ACTIVE))
                .thenReturn(Optional.of(existingOrganization));
        when(organizationRepository.findByName(updatedName.toLowerCase()))
                .thenReturn(Optional.empty());
        when(organizationServiceImpl.checkingName("test_update_organization", anyBoolean()))
                .thenReturn(true);

        // Then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> organizationServiceImpl.update(id, request))
                .withMessageContaining(ResponseMessage.ERROR_ORGANIZATION_EXISTS);

        verify(organizationRepository, times(1)).findByIdAndStatus(10L, OrganizationStatus.ACTIVE);
        verify(organizationRepository, times(1)).findByName("test_update_organization");
    }
    @Test
    void testUpdateOrganizationShouldThrowDataNotFoundException() {
        // When
        when(organizationRepository.findByIdAndStatus(id,statusActive))
                .thenThrow(new DataNotFoundException(ResponseMessage.ERROR_ORGANIZATION_NOT_FOUND_BY_ID));

        // Then
        assertThatExceptionOfType(DataNotFoundException.class)
                .isThrownBy(() -> organizationServiceImpl.update(10L,UpdateOrganizationRequest.builder().name("test_organization").build()));

        verify(organizationRepository, times(1)).findByIdAndStatus(10L, OrganizationStatus.ACTIVE);
        verifyNoMoreInteractions(organizationRepository);
    }

    @Test
    void testInactiveOrganizationShouldReturnTrue() {
        // Given
        Organization existingOrganization = new Organization();
        existingOrganization.setId(id);
        existingOrganization.setName(organizationName);
        existingOrganization.setStatus(statusActive);
        existingOrganization.setWorkers(workers);

        // When
        when(organizationRepository.findByIdAndStatus(id,statusActive))
                .thenReturn(Optional.of(existingOrganization));
        boolean result = organizationServiceImpl.inactivate(existingOrganization.getId());

        // Then
        assertTrue(result);
        assertEquals(OrganizationStatus.INACTIVE, existingOrganization.getStatus());
    }

    @Test
    void testInactiveOrganizationShouldThrowDataNotFoundException() {
        // When
        when(organizationRepository.findByIdAndStatus(id,statusActive))
                .thenThrow(new DataNotFoundException(ResponseMessage.ERROR_ORGANIZATION_NOT_FOUND_BY_ID));

        // Then
        assertThatExceptionOfType(DataNotFoundException.class)
                .isThrownBy(() -> organizationServiceImpl.inactivate(10L));

        verify(organizationRepository, times(1)).findByIdAndStatus(10L, OrganizationStatus.ACTIVE);
        verifyNoMoreInteractions(organizationRepository);
    }

    @Test
    void testDeleteShouldReturnTrue() {
        // Given
        Organization existingOrganization = new Organization();
        existingOrganization.setId(id);
        existingOrganization.setName(organizationName);
        existingOrganization.setStatus(statusActive);
        existingOrganization.setWorkers(workers);

        // When
        when(organizationRepository.findById(id))
                .thenReturn(Optional.of(existingOrganization));

        // Then
        assertTrue(organizationServiceImpl.delete(10L));

        verify(organizationRepository, times(1)).findById(10L);
        verify(organizationRepository, times(1)).delete(existingOrganization);
        verifyNoMoreInteractions(organizationRepository);
    }

    @Test
    void testDeleteShouldThrowIllegalArgumentException() {
        // When
        when(organizationRepository.findById(id))
                .thenThrow(new DataNotFoundException(ResponseMessage.ERROR_ORGANIZATION_NOT_FOUND_BY_ID));

        // Then
        assertThatExceptionOfType(DataNotFoundException.class)
                .isThrownBy(() -> organizationServiceImpl.delete(10L));

        verify(organizationRepository, times(1)).findById(10L);
        verifyNoMoreInteractions(organizationRepository);
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