package com.travelvcommerce.personalizedservice.service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public interface KafkaHistoryInfoConsumerService {
    @KafkaListener(topics = "user-delete")
    @Transactional
    void userDelete(String payload,  Acknowledgment acknowledgment);

    @KafkaListener(topics = "uploader-delete")
    @Transactional
    void uploaderDelete(String payload, Acknowledgment acknowledgment);

    @KafkaListener(topics = "video-delete")
    @Transactional
    void videoDelete(String payload, Acknowledgment acknowledgment);
}
