package com.tavelvcommerce.commentservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tavelvcommerce.commentservice.dto.VideoInfoDto;
import com.tavelvcommerce.commentservice.entitiy.Video;
import com.tavelvcommerce.commentservice.repository.CommentRepository;
import com.tavelvcommerce.commentservice.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaVideoInfoConsumerServiceImpl implements KafkaVideoInfoConsumerService {
    private final VideoRepository videoRepository;
    private final ObjectMapper objectMapper;

    @Override
    @KafkaListener(topics = "video-create")
    @Transactional
    public void createVideo(String payload) {
        log.info("received payload='{}'", payload);

        VideoInfoDto.VideoCreateDto videoCreateDto;
        try {
            videoCreateDto = objectMapper.readValue(payload, VideoInfoDto.VideoCreateDto.class);
        } catch (Exception e) {
            log.error("Error converting payload to video", e);
            return;
        }

        Video video = Video.builder()
                .videoId(videoCreateDto.getVideoId())
                .sellerId(videoCreateDto.getSellerId())
                .build();

        try {
            videoRepository.save(video);
        } catch (Exception e) {
            log.error("Error creating video", e);
            return;
        }
    }

    @Override
    @KafkaListener(topics = "video-delete")
    @Transactional
    public void deleteVideo(String payload) {
        log.info("received payload='{}'", payload);

        String videoId = payload;

        try {
            videoRepository.deleteAllByVideoId(videoId);
        } catch (Exception e) {
            log.error("Error deleting video comment", e);
            return;
        }
    }
}
