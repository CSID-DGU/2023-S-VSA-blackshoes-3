package com.travelvcommerce.personalizedservice.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelvcommerce.personalizedservice.repository.LikeVideoRepository;
import com.travelvcommerce.personalizedservice.repository.ViewVideoRepository;
import com.travelvcommerce.personalizedservice.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaHistoryInfoConsumerServiceImpl implements KafkaHistoryInfoConsumerService{

    private final ObjectMapper objectMapper;
    private final VideoService videoService;
    private final LikeVideoRepository likeVideoRepository;
    private final ViewVideoRepository viewVideoRepository;

    @Override
    @KafkaListener(topics = "user-delete")
    @Transactional
    public void userDelete(String payload, Acknowledgment acknowledgment) {
        log.info("received payload='{}'", payload);

        String userId = payload;

        viewVideoRepository.deleteByUserId(userId);
        likeVideoRepository.deleteByUserId(userId);

        acknowledgment.acknowledge();
    }

    @Override
    @KafkaListener(topics = "uploader-delete")
    @Transactional
    public void uploaderDelete(String payload, Acknowledgment acknowledgment){
        log.info("received payload='{}'", payload);

        String uploaderId = payload;

        viewVideoRepository.deleteBySellerId(uploaderId);
        likeVideoRepository.deleteBySellerId(uploaderId);

        acknowledgment.acknowledge();
    }

    @Override
    @KafkaListener(topics = "video-delete")
    @Transactional
    public void videoDelete(String payload, Acknowledgment acknowledgment){
        log.info("received payload='{}'", payload);

        String videoId = payload;

        viewVideoRepository.deleteByVideoId(videoId);
        likeVideoRepository.deleteByVideoId(videoId);

        acknowledgment.acknowledge();
    }
}
