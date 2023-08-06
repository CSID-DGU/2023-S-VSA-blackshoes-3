package com.travelvcommerce.uploadservice.service;

public interface KafkaUploaderInfoConsumerService {
    void createUploader(String payload);
    void updateUploader(String payload);
    void deleteUploader(String payload);
}
