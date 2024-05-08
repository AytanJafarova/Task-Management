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

    private Page<Organization> ORGANIZATIONS= new PageImpl<>(new ArrayList<>());

    private final Long ID = 1L;
    private final String ORGANIZATION_NAME = "test_organization";
    private final OrganizationStatus STATUS_ACTIVE = OrganizationStatus.ACTIVE;
    private final List<Worker> WORKERS = new ArrayList<>();
    private final Organization ORGANIZATION= new Organization();
    private final Pageable PAGEABLE = PageRequest.of(0,1);

    @BeforeEach
    void setUp() {
        ORGANIZATION.setId(ID);
        ORGANIZATION.setName(ORGANIZATION_NAME);
        ORGANIZATION.setStatus(STATUS_ACTIVE);
        ORGANIZATION.setWorkers(WORKERS);
        ORGANIZATIONS = new PageImpl<>(Collections.singletonList(ORGANIZATION));
    }

    @Test
    void findByStatus()
    {
        // given
        when(organizationRepository.findByStatus(any(OrganizationStatus.class), any(Pageable.class))).thenReturn(ORGANIZATIONS);

        // when
        Page<Organization> result = organizationRepository.findByStatus(STATUS_ACTIVE,PAGEABLE);

        // then
        assertThat(ORGANIZATIONS).isEqualTo(result);
    }

    @Test
    void findByIdAndStatus()
    {
        // given
        when(organizationRepository.findByIdAndStatus(anyLong(),any(OrganizationStatus.class))).thenReturn(Optional.of(ORGANIZATION));

        // when
        Optional<Organization> result = organizationRepository.findByIdAndStatus(ID,STATUS_ACTIVE);

        // then
        assertThat(ORGANIZATION).isEqualTo(result.get());
    }

    @Test
    void findByName()
    {
        // given
        when(organizationRepository.findByName(anyString())).thenReturn(Optional.of(ORGANIZATION));

        // when
        Optional<Organization> result = organizationRepository.findByName(ORGANIZATION_NAME);

        // then
        assertThat(ORGANIZATION).isEqualTo(result.get());
    }

    @Test
    void findAllWithWorkers()
    {
        // given
        when(organizationRepository.findAllWithWorkers(any(Pageable.class))).thenReturn(ORGANIZATIONS);

        // when
        Page<Organization> result = organizationRepository.findAllWithWorkers(PAGEABLE);

        // then
        assertThat(ORGANIZATIONS).isEqualTo(result);
    }
}