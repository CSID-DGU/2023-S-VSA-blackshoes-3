package com.travelvcommerce.contentslaveservice.service;

public interface KafkaStatisticsInfoConsumerService {
    void updateStatistics(String payload);
}
