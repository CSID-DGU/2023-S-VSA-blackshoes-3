package com.travelvcommerce.personalizedservice.service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public interface KafkaHistoryInfoConsumerService {
    @KafkaListener(topics = "user-delete")
    @Transactional
    void userDelete(String payload);

    @KafkaListener(topics = "uploader-delete")
    @Transactional
    void uploaderDelete(String payload);

    @KafkaListener(topics = "video-delete")
    @Transactional
    void videoDelete(String payload);
}
