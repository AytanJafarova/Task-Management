package com.tms.TaskManagementSystem.scheduler;

import com.tms.TaskManagementSystem.entity.enums.TaskStatus;
import com.tms.TaskManagementSystem.kafka.producer.MessageProducer;
import com.tms.TaskManagementSystem.response.Task.TaskResponse;
import com.tms.TaskManagementSystem.services.impl.TaskServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskScheduling {
    private final TaskServiceImpl taskService;
    private final MessageProducer messageProducer;

    // 0 0 1-6 * * *
    @Scheduled(cron = "0 0 1-6 * * *")
    @Transactional
    public void deadlineCheckingInProgress() {
       List<TaskResponse> inProgress = taskService.deadlineCheckingInProgress();
       for(TaskResponse task:inProgress)
       {
           messageProducer.publish("task_deadline_topic",task);
       }
    }

    @Scheduled(cron = "0 0 1-6 * * *")
    @Transactional
    public void deadlineCheckingOverdue()
    {
        List<TaskResponse> overDue = taskService.deadlineCheckingOverdue();
        if (!overDue.isEmpty()) {
            for(TaskResponse task:overDue)
            {
                messageProducer.publish("task_deadline_topic",task);
            }
        }
    }

    @Scheduled(cron = "0 0 1-6 * * *")
    @Transactional
    public void assignAlert()
    {
        List<TaskResponse> todo = taskService.getByTaskStatus(TaskStatus.TO_DO);
        if(!todo.isEmpty())
        {
            for(TaskResponse task: todo)
            {
                messageProducer.publish("task_deadline_topic",task);
            }
        }
    }
}