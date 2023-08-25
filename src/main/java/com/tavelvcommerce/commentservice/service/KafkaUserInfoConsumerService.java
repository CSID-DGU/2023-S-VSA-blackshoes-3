package com.tavelvcommerce.commentservice.service;

import org.springframework.kafka.support.Acknowledgment;

public interface KafkaUserInfoConsumerService {
    void createUserNickName(String payLoad, Acknowledgment acknowledgment);
    void updateUserNickname(String payload, Acknowledgment acknowledgment);
    void deleteUserComment(String payload, Acknowledgment acknowledgment);
}
