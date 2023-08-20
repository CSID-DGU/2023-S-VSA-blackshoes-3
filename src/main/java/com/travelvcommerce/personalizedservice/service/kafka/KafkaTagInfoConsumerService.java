package com.travelvcommerce.personalizedservice.service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public interface KafkaTagInfoConsumerService {
    //유저 삭제 시 구독 태그, 조회 태그 삭제
    @KafkaListener(topics = "user-delete")
    @Transactional
    void userDelete(String payload, Acknowledgment acknowledgment);
}
