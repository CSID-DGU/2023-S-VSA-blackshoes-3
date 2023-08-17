package com.tavelvcommerce.commentservice.service;

public interface KafkaVideoInfoConsumerService {
    void createVideo(String payload);
    void deleteVideo(String payload);
}
