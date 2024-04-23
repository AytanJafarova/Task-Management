package com.tms.TaskManagementSystem.kafka.consumer;

import com.tms.TaskManagementSystem.response.Task.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageConsumer {
    private static final String TOPIC_NAME = "task_deadline_topic";
    private static final String GROUP_ID = "task_group";
    private static final String CONTAINER_FACTORY = "containerFactory";
    @KafkaListener(topics = TOPIC_NAME, groupId = GROUP_ID, containerFactory = CONTAINER_FACTORY)
    public void consumeMessage(TaskResponse message) {
        System.out.println("Received message: " + message);
    }
}