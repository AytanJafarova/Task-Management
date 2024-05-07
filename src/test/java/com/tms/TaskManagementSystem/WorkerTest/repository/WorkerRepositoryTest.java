package com.tms.TaskManagementSystem.WorkerTest.repository;

import com.tms.TaskManagementSystem.entity.Organization;
import com.tms.TaskManagementSystem.entity.Task;
import com.tms.TaskManagementSystem.entity.Worker;
import com.tms.TaskManagementSystem.entity.enums.OrganizationStatus;
import com.tms.TaskManagementSystem.entity.enums.Role;
import com.tms.TaskManagementSystem.entity.enums.WorkerStatus;
import com.tms.TaskManagementSystem.repository.WorkerRepository;
import jakarta.persistence.*;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkerRepositoryTest {

    @Mock
    WorkerRepository workerRepository;

    private final Long id = 1L;
    private final String username = "test_username";
    private final String password = "test_password";
    private final String name = "test_name";
    private final String surname = "test_surname";
    private final String email = "test_email";
    private final WorkerStatus status = WorkerStatus.ACTIVE;
    private final List<Worker> workersForOrganization = new ArrayList<>();
    private Page<Worker> workers= new PageImpl<>(new ArrayList<>());
    private final List<Task> tasks = new ArrayList<>();
    private final Role role = Role.USER;
    private final Pageable pageable = PageRequest.of(0,1);
    private final Organization organization = Organization.builder()
            .id(1L)
            .name("test_organization")
            .status(OrganizationStatus.ACTIVE)
            .workers(workersForOrganization)
            .build();

    private final Worker worker = new Worker();

    @BeforeEach
    void setUp() {
        worker.setId(id);
        worker.setUsername(username);
        worker.setPassword(password);
        worker.setName(name);
        worker.setSurname(surname);
        worker.setEmail(email);
        worker.setStatus(status);
        workersForOrganization.add(worker);
        worker.setOrganization(organization);
        worker.setRole(role);
        worker.setTasks(tasks);
        workers = new PageImpl<>(Collections.singletonList(worker));
    }
    @Test
    void findByStatusAndRole()
    {
        // given
        when(workerRepository.findByStatusAndRole(any(), any(),any(Pageable.class))).thenReturn(workers);

        // when
        Page<Worker> result = workerRepository.findByStatusAndRole(status, role, pageable);

        // then
        assertThat(workers).isEqualTo(result);
    }

    @Test
    void findByRole()
    {
        // given
        when(workerRepository.findByRole(any(),any(Pageable.class))).thenReturn(workers);

        // when
        Page<Worker> result = workerRepository.findByRole(role, pageable);

        // then
        assertThat(workers).isEqualTo(result);
    }
    @Test
    void findByIdAndRole()
    {
        // given
        when(workerRepository.findByIdAndRole(anyLong(),any())).thenReturn(Optional.of(worker));

        // when
        Optional<Worker> result = workerRepository.findByIdAndRole(id, role);

        // then
        assertThat(worker).isEqualTo(result.get());
    }
    @Test
    void findByUsernameAndStatus()
    {
        // given
        when(workerRepository.findByUsernameAndStatus(anyString(),any())).thenReturn(Optional.of(worker));

        // when
        Optional<Worker> result = workerRepository.findByUsernameAndStatus(username, status);

        // then
        assertThat(worker).isEqualTo(result.get());
    }
    @Test
    void findByIdAndStatus()
    {
        // given
        when(workerRepository.findByIdAndStatus(anyLong(),any())).thenReturn(Optional.of(worker));

        // when
        Optional<Worker> result = workerRepository.findByIdAndStatus(id, status);

        // then
        assertThat(worker).isEqualTo(result.get());
    }

    @Test
    void findByEmail()
    {
        // given
        when(workerRepository.findByEmail(anyString())).thenReturn(Optional.of(worker));

        // when
        Optional<Worker> result = workerRepository.findByEmail(email);

        // then
        assertThat(worker).isEqualTo(result.get());
    }

    @Test
    void findByIdAndStatusAndRole()
    {
        // given
        when(workerRepository.findByIdAndStatusAndRole(anyLong(),any(),any())).thenReturn(Optional.of(worker));

        // when
        Optional<Worker> result = workerRepository.findByIdAndStatusAndRole(id,status,role);

        // then
        assertThat(worker).isEqualTo(result.get());
    }

    @Test
    void findByOrganizationIdAndStatus()
    {
        // given
        when(workerRepository.findByOrganizationIdAndStatus(anyLong(),any(),any(Pageable.class))).thenReturn(workers);

        // when
        Page<Worker> result = workerRepository.findByOrganizationIdAndStatus(id,status,pageable);

        // then
        assertThat(workers).isEqualTo(result);
    }
}