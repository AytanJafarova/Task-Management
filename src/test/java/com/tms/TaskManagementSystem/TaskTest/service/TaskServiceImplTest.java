package com.tms.TaskManagementSystem.TaskTest.service;

import com.tms.TaskManagementSystem.entity.Task;
import com.tms.TaskManagementSystem.entity.Worker;
import com.tms.TaskManagementSystem.entity.enums.*;
import com.tms.TaskManagementSystem.exception.DataNotFoundException;
import com.tms.TaskManagementSystem.exception.IllegalArgumentException;
import com.tms.TaskManagementSystem.exception.response.ResponseMessage;
import com.tms.TaskManagementSystem.repository.TaskRepository;
import com.tms.TaskManagementSystem.repository.WorkerRepository;
import com.tms.TaskManagementSystem.request.Task.CreateTaskRequest;
import com.tms.TaskManagementSystem.request.Task.UpdateTaskRequest;
import com.tms.TaskManagementSystem.request.Worker.UpdateWorkerRequest;
import com.tms.TaskManagementSystem.response.Task.TaskListResponse;
import com.tms.TaskManagementSystem.response.Task.TaskResponse;
import com.tms.TaskManagementSystem.response.Worker.WorkerResponse;
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
import static org.mockito.Mockito.*;

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
    private final Worker WORKER = Worker.builder()
            .name("NAME")
            .surname("SURNAME")
            .email("EMAIL")
            .password("PASSWORD")
            .username("USERNAME").build();

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
    void testSaveTaskShouldReturnTask() {
        CreateTaskRequest request = CreateTaskRequest.builder()
                .header(HEADER)
                .content(CONTENT)
                .deadline(DEADLINE)
                .priority(PRIORITY)
                .build();

        Task savedTask = Task.builder().id(ID)
                .header(HEADER)
                .content(CONTENT)
                .deadline(DEADLINE)
                .priority(PRIORITY).build();
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);
        TaskResponse response = taskServiceImpl.save(request);
        assertNotNull(response);
        verify(taskRepository).save(savedTask);
        assertEquals(ID, response.getId());
    }

    @Test
    void testUpdateTaskShouldReturnTask() {
        UpdateTaskRequest request = UpdateTaskRequest.builder()
                .header(HEADER)
                .content(CONTENT)
                .deadline(DEADLINE)
                .priority(PRIORITY)
                .build();
        Task savedTask = Task.builder().id(ID)
                .header(HEADER)
                .content(CONTENT)
                .deadline(DEADLINE)
                .priority(PRIORITY).build();

        when(taskRepository.findById(anyLong()))
                .thenReturn(Optional.of(savedTask));
        when(taskRepository.save(savedTask)).thenReturn(savedTask);

        TaskResponse response = taskServiceImpl.update(10L,request);

        assertNotNull(response);
        verify(taskRepository).findById(10L);
        verify(taskRepository).save(savedTask);
    }

    @Test
    void testUpdateTaskShouldThrowDataNotFoundException() {
        // When
        when(taskRepository.findById(anyLong()))
                .thenThrow(new DataNotFoundException(ResponseMessage.ERROR_TASK_NOT_FOUND_BY_ID));

        // Then
        assertThatExceptionOfType(DataNotFoundException.class)
                .isThrownBy(() -> taskServiceImpl.update(ID, UpdateTaskRequest.builder()
                        .header(HEADER)
                        .content(CONTENT)
                        .deadline(DEADLINE)
                        .priority(PRIORITY)
                        .build()));

        verify(taskRepository, times(1)).findById(ID);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void testUpdateTaskShouldThrowInvalidOperationException() {
        // When
        UpdateTaskRequest request = UpdateTaskRequest.builder()
                .header(HEADER)
                .content(CONTENT)
                .deadline(DEADLINE)
                .priority(PRIORITY)
                .build();
        Task savedTask = Task.builder().id(ID)
                .header(HEADER)
                .content(CONTENT)
                .deadline(DEADLINE)
                .priority(PRIORITY).build();

        when(taskRepository.findById(anyLong()))
                .thenReturn(Optional.of(savedTask));

        when(taskRepository.save(savedTask))
                .thenThrow(new IllegalArgumentException(ResponseMessage.ERROR_INVALID_OPERATION));
        // Then

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> taskServiceImpl.update(ID, request));

        verify(taskRepository, times(1)).findById(ID);
        verify(taskRepository, times(1)).save(savedTask);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void testAssignShouldThrowDataNotFoundException() {
        when(taskRepository.findById(anyLong())).thenThrow(new DataNotFoundException("Task not found by given id"));
        assertThatExceptionOfType(DataNotFoundException.class).isThrownBy(()->taskServiceImpl.assign(ID,10L));
    }

    @Test
    void testAssignShouldThrowWorkerDataNotFoundException() // review
    {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(TASK));
        assertThatExceptionOfType(DataNotFoundException.class).isThrownBy(()->taskServiceImpl.assign(ID,10L));
    }

    @Test
    void testAssignShouldThrowTaskAlreadyAssigned()
    {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(TASK));
        when(workerRepository.findById(anyLong())).thenReturn(Optional.of(WORKER));


    }

    @Test
    void testCloseShouldReturnDataNotFoundException()
    {
        // given
        when(taskRepository.findById(anyLong())).thenThrow(new DataNotFoundException("Task not found by given id"));
        // when
        assertThatExceptionOfType(DataNotFoundException.class).isThrownBy(()->taskServiceImpl.close(ID));
    }

    @Test
    void testGetTaskByWorkerIDShouldReturnTasks()
    {
        when(workerRepository.findByIdAndStatus(anyLong(),any(WorkerStatus.class))).thenReturn(Optional.of(WORKER));
        when(taskRepository.findByWorkerId(anyLong(),any(Pageable.class))).thenReturn(TASKS);

        TaskListResponse response = taskServiceImpl.getByWorkerId(10L, PAGEABLE);
        assertNotNull(response);
    }

    @Test
    void testGetTaskByWorkerIDShouldThrowWorkerNotFoundException()
    {
        // given
        when(workerRepository.findByIdAndStatus(anyLong(),any(WorkerStatus.class))).thenThrow(new DataNotFoundException("Worker not found by given id"));;

        // Then
        assertThatExceptionOfType(DataNotFoundException.class).isThrownBy(()->taskServiceImpl.getByWorkerId(10L,PAGEABLE));
    }

    @Test
    void testDeleteShouldReturnTrue() {
        // When
        when(taskRepository.findById(anyLong()))
                .thenReturn(Optional.of(TASK));

        // Then
        assertTrue(taskServiceImpl.delete(10L));

        verify(taskRepository, times(1)).findById(10L);
        verify(taskRepository, times(1)).delete(TASK);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void testDeleteShouldThrowIllegalArgumentException() {
        // When
        when(taskRepository.findById(anyLong()))
                .thenThrow(new DataNotFoundException(ResponseMessage.ERROR_TASK_NOT_FOUND_BY_ID));

        // Then
        assertThatExceptionOfType(DataNotFoundException.class)
                .isThrownBy(() -> taskServiceImpl.delete(10L));

        verify(taskRepository, times(1)).findById(10L);
        verifyNoMoreInteractions(taskRepository);
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
    void testGetTaskByIdShouldThrowDataNotFoundException() {
        // When
        when(taskRepository.findById(anyLong())).thenThrow(new DataNotFoundException("Task not found by given id"));

        // Then
        assertThatExceptionOfType(DataNotFoundException.class).isThrownBy(()->taskServiceImpl.getTaskById(10L));
    }
}