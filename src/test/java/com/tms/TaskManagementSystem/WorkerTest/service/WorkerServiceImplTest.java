package com.tms.TaskManagementSystem.WorkerTest.service;

import com.tms.TaskManagementSystem.entity.Organization;
import com.tms.TaskManagementSystem.entity.Task;
import com.tms.TaskManagementSystem.entity.Worker;
import com.tms.TaskManagementSystem.entity.enums.OrganizationStatus;
import com.tms.TaskManagementSystem.entity.enums.Role;
import com.tms.TaskManagementSystem.entity.enums.WorkerStatus;
import com.tms.TaskManagementSystem.exception.DataNotFoundException;
import com.tms.TaskManagementSystem.exception.IllegalArgumentException;
import com.tms.TaskManagementSystem.exception.response.ResponseMessage;
import com.tms.TaskManagementSystem.repository.OrganizationRepository;
import com.tms.TaskManagementSystem.repository.WorkerRepository;
import com.tms.TaskManagementSystem.request.Worker.UpdateWorkerRequest;
import com.tms.TaskManagementSystem.response.Worker.WorkerListResponse;
import com.tms.TaskManagementSystem.response.Worker.WorkerResponse;
import com.tms.TaskManagementSystem.services.WorkerService;
import com.tms.TaskManagementSystem.services.impl.WorkerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class WorkerServiceImplTest {
    @Mock
    WorkerRepository workerRepository;
    @Mock
    OrganizationRepository organizationRepository;
    @Mock
    WorkerService workerService;
    @InjectMocks
    WorkerServiceImpl workerServiceImpl;

    private final Long ID = 1L;
    private final String USERNAME = "test_username";
    private final String PASSWORD = "test_password";
    private final String NAME = "test_name";
    private final String SURNAME = "test_surname";
    private final String EMAIL = "test_email";
    private final WorkerStatus STATUS = WorkerStatus.ACTIVE;
    private final List<Worker> WORKERS_FOR_ORGANIZATION = new ArrayList<>();
    private Page<Worker> WORKERS = new PageImpl<>(new ArrayList<>());
    private final List<Task> TASKS = new ArrayList<>();
    private final Role ROLE = Role.USER;
    private final Pageable PAGEABLE = PageRequest.of(0, 1);
    private final Organization ORGANIZATION = Organization.builder()
            .id(1L)
            .name("test_organization")
            .status(OrganizationStatus.ACTIVE)
            .workers(WORKERS_FOR_ORGANIZATION)
            .build();

    private final Worker WORKER = new Worker();

    @BeforeEach
    void setUp() {
        WORKER.setId(ID);
        WORKER.setUsername(USERNAME);
        WORKER.setPassword(PASSWORD);
        WORKER.setName(NAME);
        WORKER.setSurname(SURNAME);
        WORKER.setEmail(EMAIL);
        WORKER.setStatus(STATUS);
        WORKERS_FOR_ORGANIZATION.add(WORKER);
        WORKER.setOrganization(ORGANIZATION);
        WORKER.setRole(ROLE);
        WORKER.setTasks(TASKS);
        WORKERS = new PageImpl<>(Collections.singletonList(WORKER));
    }

    @Test
    void testGetWorkerByOrganizationIDShouldReturnWorkers() {
        // given
        when(organizationRepository.findByIdAndStatus(anyLong(),any(OrganizationStatus.class))).thenReturn(Optional.of(ORGANIZATION));
        when(workerRepository.findByOrganizationIdAndStatus(anyLong(),any(WorkerStatus.class),any(Pageable.class))).thenReturn(WORKERS);

        // When
        WorkerListResponse response = workerServiceImpl.getWorkersByOrganizationId(10L,PAGEABLE);

        // Then
        assertNotNull(response);
    }
    @Test
    void testGetWorkerByOrganizationIDShouldThrowOrganizationNotFoundException() {
        // given
        when(organizationRepository.findByIdAndStatus(anyLong(),any(OrganizationStatus.class))).thenThrow(new DataNotFoundException("Organization not found by given id"));;

        // Then
        assertThatExceptionOfType(DataNotFoundException.class).isThrownBy(()->workerServiceImpl.getWorkersByOrganizationId(10L,PAGEABLE));
    }
    @Test
    void testCheckingNameValidShouldReturnTrue() {
        // Given
        when(workerService.checkingUserCredentials(anyString(),anyString(),anyString(),anyBoolean(),anyBoolean())).thenReturn(true);

        // When
        Boolean checkingResult = workerService.checkingUserCredentials(USERNAME,PASSWORD,EMAIL,true,true);

        // Then
        assertThat(checkingResult).isTrue();
        verify(workerService, times(1)).checkingUserCredentials(USERNAME,PASSWORD,EMAIL,true,true);
        verifyNoMoreInteractions(workerService);
    }

    @Test
    void testCheckingNameInvalidShouldThrowIllegalArgumentExceptionWhenNameExists() {
        // Given
        when(workerRepository.findByUsernameAndStatus(USERNAME,STATUS)).thenThrow(new IllegalArgumentException("Worker with the specified username already exists"));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> workerService.checkingUserCredentials(USERNAME,PASSWORD,EMAIL,true,true));

        verify(workerRepository, times(1)).findByUsernameAndStatus(USERNAME,STATUS);
        verifyNoMoreInteractions(workerRepository);
    }

    @Test
    void testUpdateWorkerShouldReturnWorker() {
        UpdateWorkerRequest request = UpdateWorkerRequest.builder()
                .name(NAME)
                .surname(SURNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .username(USERNAME).build();
        Worker savedWorker = Worker.builder().id(ID)
                .name(NAME)
                .surname(SURNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .username(USERNAME).build();

        when(workerRepository.findByIdAndStatusAndRole(anyLong(),any(WorkerStatus.class),any(Role.class)))
                .thenReturn(Optional.of(savedWorker));
        when(workerRepository.save(savedWorker)).thenReturn(savedWorker);

        WorkerResponse response = workerServiceImpl.update(10L,request);

        assertNotNull(response);
        verify(workerRepository).findByIdAndStatusAndRole(10L,STATUS,ROLE);
        verify(workerRepository).save(savedWorker);
    }

    @Test
    void testUpdateWorkerShouldThrowInvalidOperationException() {
        // When
        UpdateWorkerRequest request = UpdateWorkerRequest.builder()
                .name(NAME)
                .surname(SURNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .username(USERNAME).build();
        Worker savedWorker = Worker.builder().id(ID)
                .name(NAME)
                .surname(SURNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .username(USERNAME).build();

        when(workerRepository.findByIdAndStatusAndRole(anyLong(),any(WorkerStatus.class),any(Role.class)))
                .thenReturn(Optional.of(savedWorker));

        when(workerRepository.save(savedWorker))
                .thenThrow(new IllegalArgumentException(ResponseMessage.ERROR_INVALID_OPERATION));
        // Then

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> workerServiceImpl.update(ID, request));

        verify(workerRepository, times(1)).findByIdAndStatusAndRole(ID, STATUS, ROLE);
        verify(workerRepository, times(1)).save(savedWorker);
        verifyNoMoreInteractions(workerRepository);
    }
    @Test
    void testUpdateWorkerShouldThrowDataNotFoundException() {
        // When
        when(workerRepository.findByIdAndStatusAndRole(anyLong(),any(WorkerStatus.class),any(Role.class)))
                .thenThrow(new DataNotFoundException(ResponseMessage.ERROR_WORKER_NOT_FOUND_BY_ID));

        // Then
        assertThatExceptionOfType(DataNotFoundException.class)
                .isThrownBy(() -> workerServiceImpl.update(ID, UpdateWorkerRequest.builder().name("test_worker").build()));

        verify(workerRepository, times(1)).findByIdAndStatusAndRole(ID, STATUS, ROLE);
        verifyNoMoreInteractions(workerRepository);
    }

    @Test
    void testGetWorkerByIdShouldReturnWorker() {
        // Given
        when(workerRepository.findByIdAndStatusAndRole(anyLong(),any(WorkerStatus.class),any(Role.class))).thenReturn(Optional.of(WORKER));

        // When
        WorkerResponse response = workerServiceImpl.getWorkerById(10L);

        // Then
        assertNotNull(response);
    }

    @Test
    void testGetWorkerByIdShouldThrowDataNotFoundException() {
        // When
        when(workerRepository.findByIdAndStatusAndRole(anyLong(),any(WorkerStatus.class),any(Role.class))).thenThrow(new DataNotFoundException("Worker not found by given id"));

        // Then
        assertThatExceptionOfType(DataNotFoundException.class).isThrownBy(()->workerServiceImpl.getWorkerById(10L));
    }

    @Test
    void testInactiveOrganizationShouldReturnTrue() {
        // When
        when(workerRepository.findByIdAndStatusAndRole(anyLong(),any(WorkerStatus.class),any(Role.class)))
                .thenReturn(Optional.of(WORKER));
        boolean result = workerServiceImpl.inactivate(WORKER.getId());

        // Then
        assertTrue(result);
        assertEquals(WorkerStatus.INACTIVE, WORKER.getStatus());
    }

    @Test
    void testInactiveWorkerShouldThrowDataNotFoundException() {
        // When
        when(workerRepository.findByIdAndStatusAndRole(anyLong(),any(WorkerStatus.class),any(Role.class)))
                .thenThrow(new DataNotFoundException(ResponseMessage.ERROR_WORKER_NOT_FOUND_BY_ID));

        // Then
        assertThatExceptionOfType(DataNotFoundException.class)
                .isThrownBy(() -> workerServiceImpl.inactivate(10L));

        verify(workerRepository, times(1)).findByIdAndStatusAndRole(10L, STATUS,ROLE);
        verifyNoMoreInteractions(workerRepository);
    }

    @Test
    void testDeleteShouldReturnTrue() {
        // Given
        Worker existingWorker = new Worker();
        existingWorker.setId(ID);
        existingWorker.setUsername(USERNAME);
        existingWorker.setPassword(PASSWORD);
        existingWorker.setName(NAME);
        existingWorker.setSurname(SURNAME);
        existingWorker.setEmail(EMAIL);
        existingWorker.setStatus(STATUS);

        // When
        when(workerRepository.findByIdAndRole(anyLong(), any(Role.class)))
                .thenReturn(Optional.of(existingWorker));

        // Then
        assertTrue(workerServiceImpl.delete(10L));

        verify(workerRepository, times(1)).findByIdAndRole(10L,ROLE);
        verify(workerRepository, times(1)).delete(existingWorker);
        verifyNoMoreInteractions(workerRepository);
    }

    @Test
    void testDeleteShouldThrowIllegalArgumentException() {
        // When
        when(workerRepository.findByIdAndRole(anyLong(),any(Role.class)))
                .thenThrow(new DataNotFoundException(ResponseMessage.ERROR_WORKER_NOT_FOUND_BY_ID));

        // Then
        assertThatExceptionOfType(DataNotFoundException.class)
                .isThrownBy(() -> workerServiceImpl.delete(10L));

        verify(workerRepository, times(1)).findByIdAndRole(10L,ROLE);
        verifyNoMoreInteractions(workerRepository);
    }
}