package com.tms.TaskManagementSystem.repository;

import com.tms.TaskManagementSystem.entity.Organization;
import com.tms.TaskManagementSystem.entity.enums.OrganizationStatus;
import org.hibernate.query.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationRepository  extends JpaRepository<Organization, Long> {
    List<Organization> findByStatus(OrganizationStatus status, Pageable pageable);
    @Query("SELECT o FROM Organization o LEFT JOIN FETCH o.workers WHERE o.status ='ACTIVE'")
    List<Organization> findAllWithWorkers(Pageable pageable);
    Optional<Organization> findByIdAndStatus(Long id, OrganizationStatus status);
    Optional<Organization> findByName(String name);
}
