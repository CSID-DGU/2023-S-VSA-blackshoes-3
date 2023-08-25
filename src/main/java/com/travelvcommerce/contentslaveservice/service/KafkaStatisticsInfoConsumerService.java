package com.travelvcommerce.contentslaveservice.service;

import org.springframework.kafka.support.Acknowledgment;

public interface KafkaStatisticsInfoConsumerService {
    void updateStatistics(String payload, Acknowledgment acknowledgment);
}
