package com.tms.TaskManagementSystem.repository;

import com.tms.TaskManagementSystem.entity.Worker;
import com.tms.TaskManagementSystem.entity.enums.WorkerStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkerRepository extends JpaRepository<Worker,Long> {
    List<Worker> findByStatus(WorkerStatus status, Pageable pageable);
    Optional<Worker> findByUsername(String username);
    Optional<Worker> findByIdAndStatus(Long id, WorkerStatus status);
    List<Worker> findByOrganizationIdAndStatus(Long id,WorkerStatus status,Pageable pageable);
}
