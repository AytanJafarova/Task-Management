package com.tms.TaskManagementSystem.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="workers")
public class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;
    private String name;
    private String surname;

    @Column(unique = true)
    private String email;
    private boolean isDeleted;

    @ManyToOne
    private Organization organization;

    @OneToMany(mappedBy = "worker",cascade = CascadeType.ALL)
    private List<Task> tasks;
}
