package com.tms.TaskManagementSystem.TaskTest.repository;

import com.tms.TaskManagementSystem.entity.Organization;
import com.tms.TaskManagementSystem.entity.Task;
import com.tms.TaskManagementSystem.entity.enums.OrganizationStatus;
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

    private Page<Task> tasks= new PageImpl<>(new ArrayList<>());
    private final Long id = 1L;
    private final String header = "test_header";
    private final String content = "test_header";
    private final TaskPriority priority = TaskPriority.HIGH;
    private final TaskStatus status = TaskStatus.TO_DO;
    private final LocalDateTime created = LocalDateTime.now();
    private final LocalDateTime deadline = LocalDateTime.now().plusDays(1);
    private final LocalDateTime closed = LocalDateTime.now().plusDays(2);

    private final Task task= new Task();
    private final Pageable pageable = PageRequest.of(0,1);


    @BeforeEach
    void setUp() {
        task.setId(id);
        task.setHeader(header);
        task.setStatus(status);
        task.setContent(content);
        task.setPriority(priority);
        task.setCreated(created);
        task.setDeadline(deadline);
        task.setClosed(closed);
        tasks = new PageImpl<>(Collections.singletonList(task));
    }

    @Test
    void findByHeader()
    {
        // given
        when(taskRepository.findByHeader(anyString())).thenReturn(Optional.of(task));

        // when
        Optional<Task> result = taskRepository.findByHeader(header);

        // then
        assertThat(task).isEqualTo(result.get());
    }

    @Test
    void findByWorkerId()
    {
        // given
        when(taskRepository.findByWorkerId(anyLong(),any(Pageable.class))).thenReturn(tasks);

        // when
        Page<Task> result = taskRepository.findByWorkerId(id,pageable);

        // then
        assertThat(tasks).isEqualTo(result);
    }

    @Test
    void findByTaskStatus()
    {
        // given
        when(taskRepository.findByStatus(any(),any(Pageable.class))).thenReturn(tasks);

        // when
        Page<Task> result = taskRepository.findByStatus(status,pageable);

        // then
        assertThat(tasks).isEqualTo(result);
    }

}