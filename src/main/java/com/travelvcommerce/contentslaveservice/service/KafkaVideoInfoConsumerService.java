package com.travelvcommerce.contentslaveservice.service;

public interface KafkaVideoInfoConsumerService {
    void createVideo(String payload);
    void updateVideo(String payload);
    void deleteVideo(String payload);
}
