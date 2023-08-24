package com.travelvcommerce.personalizedservice.service.kafka;

import com.travelvcommerce.personalizedservice.repository.SubscribedTagRepository;
import com.travelvcommerce.personalizedservice.repository.ViewTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class KafkaTagInfoConsumerServiceImpl implements KafkaTagInfoConsumerService{

    private final ViewTagRepository viewTagRepository;
    private final SubscribedTagRepository subscribedTagRepository;

    @Override
    @KafkaListener(topics = "user-delete")
    public void userDelete(String payload, Acknowledgment acknowledgment) {
        log.info("received payload='{}'", payload);

        String userId = payload;
        viewTagRepository.deleteByUserId(userId);
        subscribedTagRepository.deleteByUserId(userId);

        acknowledgment.acknowledge();
    }
}
