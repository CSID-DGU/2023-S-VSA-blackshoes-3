package com.tavelvcommerce.commentservice.service;

import com.tavelvcommerce.commentservice.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaVideoInfoConsumerServiceImpl implements KafkaVideoInfoConsumerService {
    private final CommentRepository commentRepository;

    @Override
    @KafkaListener(topics = "video-delete")
    @Transactional
    public void deleteVideoComment(String payload) {
        log.info("received payload='{}'", payload);

        String videoId = payload;

        try {
            commentRepository.deleteAllByVideoId(videoId);
        } catch (Exception e) {
            log.error("Error deleting video comment", e);
            return;
        }
    }
}
