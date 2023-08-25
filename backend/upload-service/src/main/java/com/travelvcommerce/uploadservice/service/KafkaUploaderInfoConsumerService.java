package com.travelvcommerce.uploadservice.service;

import org.springframework.kafka.support.Acknowledgment;

public interface KafkaUploaderInfoConsumerService {
    void createUploader(String payload, Acknowledgment acknowledgment);
    void updateUploader(String payload, Acknowledgment acknowledgment);
    void deleteUploader(String payload, Acknowledgment acknowledgment);
}
