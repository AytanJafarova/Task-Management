package com.tms.TaskManagementSystem.repository;

import com.tms.TaskManagementSystem.domain.Organization;
import com.tms.TaskManagementSystem.domain.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkerRepository extends JpaRepository<Worker,Long> {
    @Override
    @Query("SELECT w FROM Worker w where w.isDeleted=false")
    List<Worker> findAll();

    @Override
    @Query("SELECT w FROM Worker w WHERE w.isDeleted=false AND w.id = :id")
    Optional<Worker> findById(Long id);

    @Query("SELECT w FROM Worker w WHERE w.isDeleted=false AND w.organization.id=:id ")
    List<Worker> findByOrganizationId(Long id);
}
