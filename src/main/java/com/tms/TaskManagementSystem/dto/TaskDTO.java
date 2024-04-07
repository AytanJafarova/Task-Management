package com.tms.TaskManagementSystem.dto;

import com.tms.TaskManagementSystem.entity.enums.TaskPriority;
import com.tms.TaskManagementSystem.entity.enums.TaskStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TaskDTO {
    private Long id;
    private String header;
    private String content;
    private TaskPriority priority;
    private TaskStatus status;
    private LocalDateTime created;
    private LocalDateTime deadline;
    private LocalDateTime closed;
}
