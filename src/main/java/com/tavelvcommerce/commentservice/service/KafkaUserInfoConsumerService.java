package com.tavelvcommerce.commentservice.service;

public interface KafkaUserInfoConsumerService {
    void createUserNickName(String payLoad);
    void updateUserNickname(String payload);
    void deleteUserComment(String payload);
}
