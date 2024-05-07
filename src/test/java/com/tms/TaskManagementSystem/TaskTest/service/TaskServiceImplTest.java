package com.tms.TaskManagementSystem.TaskTest.service;

import com.tms.TaskManagementSystem.entity.Organization;
import com.tms.TaskManagementSystem.entity.Task;
import com.tms.TaskManagementSystem.entity.enums.TaskPriority;
import com.tms.TaskManagementSystem.entity.enums.TaskStatus;
import com.tms.TaskManagementSystem.exception.DataNotFoundException;
import com.tms.TaskManagementSystem.repository.TaskRepository;
import com.tms.TaskManagementSystem.repository.WorkerRepository;
import com.tms.TaskManagementSystem.response.Organization.OrganizationWorkersResponse;
import com.tms.TaskManagementSystem.response.Task.TaskListResponse;
import com.tms.TaskManagementSystem.response.Task.TaskResponse;
import com.tms.TaskManagementSystem.services.TaskService;
import com.tms.TaskManagementSystem.services.impl.TaskServiceImpl;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {
    @Mock
    TaskRepository taskRepository;
    @Mock
    WorkerRepository workerRepository;
    @Mock
    TaskService taskService;
    @InjectMocks
    TaskServiceImpl taskServiceImpl;

    private Page<Task> TASKS= new PageImpl<>(new ArrayList<>());
    private final Long ID = 1L;
    private final String HEADER = "test_header";
    private final String CONTENT = "test_header";
    private final TaskPriority PRIORITY = TaskPriority.HIGH;
    private final TaskStatus STATUS = TaskStatus.TO_DO;
    private final LocalDateTime CREATED = LocalDateTime.now();
    private final LocalDateTime DEADLINE = LocalDateTime.now().plusDays(1);
    private final LocalDateTime CLOSED = LocalDateTime.now().plusDays(2);

    private final Task TASK= new Task();
    private final Pageable PAGEABLE = PageRequest.of(0,1);
    @BeforeEach
    void setUp() {
        TASK.setId(ID);
        TASK.setHeader(HEADER);
        TASK.setStatus(STATUS);
        TASK.setContent(CONTENT);
        TASK.setPriority(PRIORITY);
        TASK.setCreated(CREATED);
        TASK.setDeadline(DEADLINE);
        TASK.setClosed(CLOSED);
        TASKS = new PageImpl<>(Collections.singletonList(TASK));
    }
    @Test
    void testGetTaskByOrganizationIdShouldReturnOrganization() {
        // Given
        when(taskRepository.findByWorkerId(anyLong(),any(Pageable.class))).thenReturn(TASKS);

        // When
        TaskListResponse response = taskServiceImpl.getByWorkerId(ID,PAGEABLE);

        // Then
        assertNotNull(response);
    }

    @Test
    void testGetTaskByOrganizationIdShouldThrowDataNotFoundException()
    {
        // When
        when(taskRepository.findByWorkerId(anyLong(),any(Pageable.class))).thenThrow(new DataNotFoundException("Worker not found by given id"));

        // Then
        assertThatExceptionOfType(DataNotFoundException.class).isThrownBy(()->taskServiceImpl.getByWorkerId(10L, PAGEABLE));
    }

    @Test
    void testGetTaskByIdShouldReturnOrganization() {
        // Given
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(TASK));

        // When
        TaskResponse response = taskServiceImpl.getTaskById(ID);

        // Then
        assertNotNull(response);
    }

    @Test
    void testGetTaskByIdShouldThrowDataNotFoundException()
    {
        // When
        when(taskRepository.findById(anyLong())).thenThrow(new DataNotFoundException("Task not found by given id"));

        // Then
        assertThatExceptionOfType(DataNotFoundException.class).isThrownBy(()->taskServiceImpl.getTaskById(10L));
    }
}