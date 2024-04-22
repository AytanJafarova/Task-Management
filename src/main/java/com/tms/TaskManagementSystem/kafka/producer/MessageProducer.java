package com.tms.TaskManagementSystem.kafka.producer;

import com.tms.TaskManagementSystem.response.Task.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageProducer {
    private final KafkaTemplate<String, TaskResponse> kafkaTemplate;

    public void publish(String topicName, TaskResponse message) {
        kafkaTemplate.send(topicName, message);
    }
}
