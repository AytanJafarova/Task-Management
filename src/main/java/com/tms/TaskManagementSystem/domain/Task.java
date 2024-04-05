package com.tms.TaskManagementSystem.domain;

import com.tms.TaskManagementSystem.enums.TaskDegree;
import com.tms.TaskManagementSystem.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String header;
    private String content;
    private TaskDegree degree;
    private TaskStatus status;
    private LocalDateTime created;
    private LocalDateTime deadline;
    private LocalDateTime closed;

    @ManyToOne
    private Worker worker;
}
