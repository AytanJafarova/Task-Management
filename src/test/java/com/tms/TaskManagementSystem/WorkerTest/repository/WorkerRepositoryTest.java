package com.tms.TaskManagementSystem.WorkerTest.repository;

import com.tms.TaskManagementSystem.entity.Organization;
import com.tms.TaskManagementSystem.entity.Task;
import com.tms.TaskManagementSystem.entity.Worker;
import com.tms.TaskManagementSystem.entity.enums.OrganizationStatus;
import com.tms.TaskManagementSystem.entity.enums.Role;
import com.tms.TaskManagementSystem.entity.enums.WorkerStatus;
import com.tms.TaskManagementSystem.repository.WorkerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkerRepositoryTest {

    @Mock
    WorkerRepository workerRepository;

    private final Long ID = 1L;
    private final String USERNAME = "test_username";
    private final String PASSWORD = "test_password";
    private final String NAME = "test_name";
    private final String SURNAME = "test_surname";
    private final String EMAIL = "test_email";
    private final WorkerStatus STATUS = WorkerStatus.ACTIVE;
    private final List<Worker> WORKERS_FOR_ORGANIZATION = new ArrayList<>();
    private Page<Worker> WORKERS= new PageImpl<>(new ArrayList<>());
    private final List<Task> TASKS = new ArrayList<>();
    private final Role ROLE = Role.USER;
    private final Pageable PAGEABLE = PageRequest.of(0,1);
    private final Organization ORGANIZATION = Organization.builder()
            .id(1L)
            .name("test_organization")
            .status(OrganizationStatus.ACTIVE)
            .workers(WORKERS_FOR_ORGANIZATION)
            .build();

    private final Worker worker = new Worker();

    @BeforeEach
    void setUp() {
        worker.setId(ID);
        worker.setUsername(USERNAME);
        worker.setPassword(PASSWORD);
        worker.setName(NAME);
        worker.setSurname(SURNAME);
        worker.setEmail(EMAIL);
        worker.setStatus(STATUS);
        WORKERS_FOR_ORGANIZATION.add(worker);
        worker.setOrganization(ORGANIZATION);
        worker.setRole(ROLE);
        worker.setTasks(TASKS);
        WORKERS = new PageImpl<>(Collections.singletonList(worker));
    }
    @Test
    void findByStatusAndRole()
    {
        // given
        when(workerRepository.findByStatusAndRole(any(), any(),any(Pageable.class))).thenReturn(WORKERS);

        // when
        Page<Worker> result = workerRepository.findByStatusAndRole(STATUS, ROLE, PAGEABLE);

        // then
        assertThat(WORKERS).isEqualTo(result);
    }

    @Test
    void findByRole()
    {
        // given
        when(workerRepository.findByRole(any(),any(Pageable.class))).thenReturn(WORKERS);

        // when
        Page<Worker> result = workerRepository.findByRole(ROLE, PAGEABLE);

        // then
        assertThat(WORKERS).isEqualTo(result);
    }
    @Test
    void findByIdAndRole()
    {
        // given
        when(workerRepository.findByIdAndRole(anyLong(),any())).thenReturn(Optional.of(worker));

        // when
        Optional<Worker> result = workerRepository.findByIdAndRole(ID, ROLE);

        // then
        assertThat(worker).isEqualTo(result.get());
    }
    @Test
    void findByUsernameAndStatus()
    {
        // given
        when(workerRepository.findByUsernameAndStatus(anyString(),any())).thenReturn(Optional.of(worker));

        // when
        Optional<Worker> result = workerRepository.findByUsernameAndStatus(USERNAME, STATUS);

        // then
        assertThat(worker).isEqualTo(result.get());
    }
    @Test
    void findByIdAndStatus()
    {
        // given
        when(workerRepository.findByIdAndStatus(anyLong(),any())).thenReturn(Optional.of(worker));

        // when
        Optional<Worker> result = workerRepository.findByIdAndStatus(ID, STATUS);

        // then
        assertThat(worker).isEqualTo(result.get());
    }

    @Test
    void findByEmail()
    {
        // given
        when(workerRepository.findByEmail(anyString())).thenReturn(Optional.of(worker));

        // when
        Optional<Worker> result = workerRepository.findByEmail(EMAIL);

        // then
        assertThat(worker).isEqualTo(result.get());
    }

    @Test
    void findByIdAndStatusAndRole()
    {
        // given
        when(workerRepository.findByIdAndStatusAndRole(anyLong(),any(),any())).thenReturn(Optional.of(worker));

        // when
        Optional<Worker> result = workerRepository.findByIdAndStatusAndRole(ID,STATUS,ROLE);

        // then
        assertThat(worker).isEqualTo(result.get());
    }

    @Test
    void findByOrganizationIdAndStatus()
    {
        // given
        when(workerRepository.findByOrganizationIdAndStatus(anyLong(),any(),any(Pageable.class))).thenReturn(WORKERS);

        // when
        Page<Worker> result = workerRepository.findByOrganizationIdAndStatus(ID,STATUS,PAGEABLE);

        // then
        assertThat(WORKERS).isEqualTo(result);
    }
}