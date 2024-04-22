package com.tms.TaskManagementSystem.repository;

import com.tms.TaskManagementSystem.entity.Worker;
import com.tms.TaskManagementSystem.entity.enums.Role;
import com.tms.TaskManagementSystem.entity.enums.WorkerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkerRepository extends JpaRepository<Worker,Long> {
    Page<Worker> findByStatusAndRole(WorkerStatus status, Role role, Pageable pageable);
    Page<Worker> findByRole(Role role, Pageable pageable);
    Optional<Worker> findByUsername(String username);
    Optional<Worker> findByIdAndStatus(Long id, WorkerStatus status);
    Page<Worker> findByOrganizationIdAndStatus(Long id, WorkerStatus status, Pageable pageable);
}
