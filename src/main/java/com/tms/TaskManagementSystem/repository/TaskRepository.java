package com.tms.TaskManagementSystem.repository;

import com.tms.TaskManagementSystem.entity.Task;
import com.tms.TaskManagementSystem.entity.enums.TaskStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
    Optional<Task> findByHeader(String header);
    List<Task> findByWorkerId(Long id, Pageable pageable);
    List<Task> findByStatus(TaskStatus status,Pageable pageable);
    Optional<Task> findByIdAndStatus(Long id, TaskStatus status);
}
