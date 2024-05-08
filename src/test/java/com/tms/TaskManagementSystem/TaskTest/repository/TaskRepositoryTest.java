package com.tms.TaskManagementSystem.TaskTest.repository;

import com.tms.TaskManagementSystem.entity.Task;
import com.tms.TaskManagementSystem.entity.enums.TaskPriority;
import com.tms.TaskManagementSystem.entity.enums.TaskStatus;
import com.tms.TaskManagementSystem.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskRepositoryTest {
    @Mock
    TaskRepository taskRepository;

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
    void findByHeader()
    {
        // given
        when(taskRepository.findByHeader(anyString())).thenReturn(Optional.of(TASK));

        // when
        Optional<Task> result = taskRepository.findByHeader(HEADER);

        // then
        assertThat(TASK).isEqualTo(result.get());
    }

    @Test
    void findByWorkerId()
    {
        // given
        when(taskRepository.findByWorkerId(anyLong(),any(Pageable.class))).thenReturn(TASKS);

        // when
        Page<Task> result = taskRepository.findByWorkerId(ID,PAGEABLE);

        // then
        assertThat(TASKS).isEqualTo(result);
    }

    @Test
    void findByTaskStatus()
    {
        // given
        when(taskRepository.findByStatus(any(),any(Pageable.class))).thenReturn(TASKS);

        // when
        Page<Task> result = taskRepository.findByStatus(STATUS,PAGEABLE);

        // then
        assertThat(TASKS).isEqualTo(result);
    }

}