package com.tavelvcommerce.commentservice.service;

import org.springframework.kafka.support.Acknowledgment;

public interface KafkaVideoInfoConsumerService {
    void createVideo(String payload, Acknowledgment acknowledgment);
    void deleteVideo(String payload, Acknowledgment acknowledgment);
}
