package com.tms.TaskManagementSystem.OrganizationTest.repository;

import com.tms.TaskManagementSystem.entity.Organization;
import com.tms.TaskManagementSystem.entity.Worker;
import com.tms.TaskManagementSystem.entity.enums.OrganizationStatus;
import com.tms.TaskManagementSystem.repository.OrganizationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrganizationRepositoryTest {

    @Mock
    OrganizationRepository organizationRepository;

    private Page<Organization> organizations= new PageImpl<>(new ArrayList<>());

    private final Long id = 1L;
    private final String organizationName = "test_organization";
    private final OrganizationStatus statusActive = OrganizationStatus.ACTIVE;
    private final List<Worker> workers = new ArrayList<>();
    private final Organization organization= new Organization();
    private final Pageable pageable = PageRequest.of(0,1);

    @BeforeEach
    void setUp() {
        organization.setId(id);
        organization.setName(organizationName);
        organization.setStatus(statusActive);
        organization.setWorkers(workers);
        organizations = new PageImpl<>(Collections.singletonList(organization));
    }

    @Test
    void findByStatus()
    {
        // given
        when(organizationRepository.findByStatus(any(OrganizationStatus.class), any(Pageable.class))).thenReturn(organizations);

        // when
        Page<Organization> result = organizationRepository.findByStatus(statusActive,pageable);

        // then
        assertThat(organizations).isEqualTo(result);
    }

    @Test
    void findByIdAndStatus()
    {
        // given
        when(organizationRepository.findByIdAndStatus(anyLong(),any(OrganizationStatus.class))).thenReturn(Optional.of(organization));

        // when
        Optional<Organization> result = organizationRepository.findByIdAndStatus(id,statusActive);

        // then
        assertThat(organization).isEqualTo(result.get());
    }

    @Test
    void findByName()
    {
        // given
        when(organizationRepository.findByName(anyString())).thenReturn(Optional.of(organization));

        // when
        Optional<Organization> result = organizationRepository.findByName(organizationName);

        // then
        assertThat(organization).isEqualTo(result.get());
    }

    @Test
    void findAllWithWorkers() // check again (query implementation checking + worker include checking)
    {
        // given
        when(organizationRepository.findAllWithWorkers(any(Pageable.class))).thenReturn(organizations);

        // when
        Page<Organization> result = organizationRepository.findAllWithWorkers(pageable);

        // then
        assertThat(organizations).isEqualTo(result);
    }
}