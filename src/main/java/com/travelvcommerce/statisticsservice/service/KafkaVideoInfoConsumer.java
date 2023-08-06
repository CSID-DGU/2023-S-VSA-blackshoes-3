package com.travelvcommerce.statisticsservice.service;

public interface KafkaVideoInfoConsumer {
    void createVideo(String payload);
    void updateVideo(String payload);
    void deleteVideo(String payload);
}
