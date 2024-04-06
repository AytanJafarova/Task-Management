package com.tms.TaskManagementSystem.entity;

import com.tms.TaskManagementSystem.entity.enums.WorkerStatus;
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

    @Enumerated(EnumType.STRING)
    private WorkerStatus status;

    @ManyToOne
    private Organization organization;

    @OneToMany(mappedBy = "worker", fetch =FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Task> tasks;
}
