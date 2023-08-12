package com.tavelvcommerce.commentservice.service;

public interface KafkaUserInfoConsumerService {
    void updateUserNickname(String payload);
    void deleteUserComment(String payload);
}
