package com.travelvcommerce.statisticsservice.service;

import org.springframework.kafka.support.Acknowledgment;

public interface KafkaVideoInfoConsumer {
    void createVideo(String payload, Acknowledgment acknowledgment);
    void updateVideo(String payload, Acknowledgment acknowledgment);
    void deleteVideo(String payload, Acknowledgment acknowledgment);
}
