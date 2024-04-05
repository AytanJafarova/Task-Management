package com.tms.TaskManagementSystem.repository;

import com.tms.TaskManagementSystem.domain.Organization;
import com.tms.TaskManagementSystem.domain.Task;
import com.tms.TaskManagementSystem.response.Task.TaskForWorkerResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {

    @Query("SELECT t FROM Task t WHERE t.header = :header")
    Optional<Task> findByHeader(String header);

    @Query("SELECT t FROM Task t WHERE t.worker.id = :id")
    List<Task> findByWorkerId(Long id);

    @Query("SELECT t FROM Task t WHERE t.closed IS NOT NULL")
    List<Task> findAllClosedTasks();
}
