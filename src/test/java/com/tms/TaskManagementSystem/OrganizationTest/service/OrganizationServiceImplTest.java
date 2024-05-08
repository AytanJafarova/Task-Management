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

    private final Long ID = 10L;
    private final String ORGANIZATION_NAME = "test_organization";
    private final Boolean IGNORE_NAME = true;
    private final OrganizationStatus STATUS_ACTIVE = OrganizationStatus.ACTIVE;
    private final List<Worker> WORKERS = new ArrayList<>();
    private final OrganizationResponse ORGANIZATION= new OrganizationResponse();

    @BeforeEach
    void setUp() {
        ORGANIZATION.setId(ID);
        ORGANIZATION.setName(ORGANIZATION_NAME);
        ORGANIZATION.setStatus(STATUS_ACTIVE);
    }

    @Test
    void testCheckingNameInvalidShouldThrowIllegalArgumentExceptionWhenNameExists() {
        // Given
        when(organizationRepository.findByName(ORGANIZATION_NAME)).thenThrow(new IllegalArgumentException("Organization with the specified name already exists"));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> organizationServiceImpl.checkingName("test_organization",anyBoolean()));

        verify(organizationRepository, times(1)).findByName("test_organization");
        verifyNoMoreInteractions(organizationRepository);
    }

    @Test
    void testCheckingNameExistingShouldThrowIllegalArgumentException() {
        // Given
        when(organizationService.checkingName(ORGANIZATION_NAME,!IGNORE_NAME)).thenThrow(new IllegalArgumentException("Organization with the specified name already exists"));

       // Then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(()->organizationService.checkingName(ORGANIZATION_NAME,false));
        verify(organizationService, times(1)).checkingName(ORGANIZATION_NAME,false);
        verifyNoMoreInteractions(organizationService);
    }

    @Test
    void testCheckingNameValidShouldReturnTrue() {
        // Given
        when(organizationService.checkingName(ORGANIZATION_NAME,IGNORE_NAME)).thenReturn(true);

        // When
        Boolean checkingResult = organizationService.checkingName(ORGANIZATION_NAME,true);

        // Then
        assertThat(checkingResult).isTrue();
        verify(organizationService, times(1)).checkingName(ORGANIZATION_NAME,true);
        verifyNoMoreInteractions(organizationService);
    }

    @Test
    void testSaveOrganizationShouldReturnOrganization() {
        // Given
        CreateOrganizationRequest request = CreateOrganizationRequest.builder()
                .name(ORGANIZATION_NAME).build();

        Organization savedOrganization = new Organization();
        savedOrganization.setId(ID);
        savedOrganization.setStatus(STATUS_ACTIVE);
        savedOrganization.setWorkers(WORKERS);
        savedOrganization.setName(ORGANIZATION_NAME);
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
                .isThrownBy(() -> organizationServiceImpl.save(CreateOrganizationRequest.builder().name(ORGANIZATION_NAME).build()));

        verify(organizationRepository, times(1))
                .save(any(Organization.class));
    }

    @Test
    void testUpdateOrganizationShouldReturnOrganization() {
        UpdateOrganizationRequest request = UpdateOrganizationRequest.builder()
                .name(ORGANIZATION_NAME).build();
        Organization savedOrganization = Organization.builder().id(ID).workers(WORKERS).status(STATUS_ACTIVE).name(ORGANIZATION_NAME).build();

        when(organizationRepository.findByIdAndStatus(ID, OrganizationStatus.ACTIVE))
                .thenReturn(Optional.of(savedOrganization));
        when(organizationRepository.save(savedOrganization)).thenReturn(savedOrganization);

        OrganizationResponse response = organizationServiceImpl.update(10L,request);

        assertNotNull(response);
        verify(organizationRepository).findByIdAndStatus(10L, STATUS_ACTIVE);
        verify(organizationRepository).save(savedOrganization);
    }

    @Test
    void testUpdateOrganizationThrowsIllegalArgumentException() {
        // given
        String updatedName = "test_update_organization";
        UpdateOrganizationRequest request = UpdateOrganizationRequest.builder()
                .name(updatedName)
                .build();

        Organization existingOrganization = new Organization();
        existingOrganization.setId(ID);
        existingOrganization.setName(ORGANIZATION_NAME);
        existingOrganization.setStatus(STATUS_ACTIVE);
        existingOrganization.setWorkers(WORKERS);

        when(organizationRepository.findByIdAndStatus(anyLong(),any(OrganizationStatus.class)))
                .thenReturn(Optional.of(existingOrganization));
        when(organizationRepository.save(existingOrganization))
                .thenThrow(new IllegalArgumentException(ResponseMessage.ERROR_INVALID_OPERATION));

        // when
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> organizationServiceImpl.update(ID, request));

        // then
        verify(organizationRepository, times(1)).findByIdAndStatus(10L, OrganizationStatus.ACTIVE);
        verify(organizationRepository, times(1)).save(existingOrganization);
    }

    @Test
    void testUpdateOrganizationShouldThrowDataNotFoundException() {
        // When
        when(organizationRepository.findByIdAndStatus(ID,STATUS_ACTIVE))
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
        existingOrganization.setId(ID);
        existingOrganization.setName(ORGANIZATION_NAME);
        existingOrganization.setStatus(STATUS_ACTIVE);
        existingOrganization.setWorkers(WORKERS);

        // When
        when(organizationRepository.findByIdAndStatus(ID,STATUS_ACTIVE))
                .thenReturn(Optional.of(existingOrganization));
        boolean result = organizationServiceImpl.inactivate(existingOrganization.getId());

        // Then
        assertTrue(result);
        assertEquals(OrganizationStatus.INACTIVE, existingOrganization.getStatus());
    }

    @Test
    void testInactiveOrganizationShouldThrowDataNotFoundException() {
        // When
        when(organizationRepository.findByIdAndStatus(ID,STATUS_ACTIVE))
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
        existingOrganization.setId(ID);
        existingOrganization.setName(ORGANIZATION_NAME);
        existingOrganization.setStatus(STATUS_ACTIVE);
        existingOrganization.setWorkers(WORKERS);

        // When
        when(organizationRepository.findById(ID))
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
        when(organizationRepository.findById(ID))
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
        existingOrganization.setId(ID);
        existingOrganization.setName(ORGANIZATION_NAME);
        existingOrganization.setStatus(STATUS_ACTIVE);
        existingOrganization.setWorkers(WORKERS);
        when(organizationRepository.findByIdAndStatus(ID,STATUS_ACTIVE)).thenReturn(Optional.of(existingOrganization));

        // When
        OrganizationWorkersResponse response = organizationServiceImpl.getOrganizationById(10L);

        // Then
        assertNotNull(response);
    }

    @Test
    void testGetOrganizationByIdShouldThrowDataNotFoundException() {
        // When
        when(organizationRepository.findByIdAndStatus(ID,STATUS_ACTIVE)).thenThrow(new DataNotFoundException("Organization not found by given id"));

       // Then
        assertThatExceptionOfType(DataNotFoundException.class).isThrownBy(()->organizationServiceImpl.getOrganizationById(10L));
    }
}