package com.tms.TaskManagementSystem.repository;

import com.tms.TaskManagementSystem.domain.Organization;
import com.tms.TaskManagementSystem.response.Organization.OrganizationGetResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationRepository  extends JpaRepository<Organization, Long> {
    @Override
    @Query("SELECT o FROM Organization o where o.isDeleted=false")
    List<Organization> findAll();

    @Query("SELECT o FROM Organization o LEFT JOIN FETCH o.workers WHERE o.isDeleted = false")
    List<Organization> findAllWithWorkers();

    @Query("SELECT id FROM Organization WHERE isDeleted = false")
    List<Long> findAllOrganizationIds();
    @Override
    @Query("SELECT o FROM Organization o WHERE o.isDeleted=false and o.id=:id")
    Optional<Organization> findById(Long id);
}
