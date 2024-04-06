package com.tms.TaskManagementSystem.entity;

import com.tms.TaskManagementSystem.entity.enums.OrganizationStatus;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="organizations")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    private OrganizationStatus status;

    @OneToMany(mappedBy = "organization",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Worker> workers;
}