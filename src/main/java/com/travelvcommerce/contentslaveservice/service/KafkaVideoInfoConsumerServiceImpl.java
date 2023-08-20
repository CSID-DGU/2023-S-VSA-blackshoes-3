package com.travelvcommerce.contentslaveservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelvcommerce.contentslaveservice.dto.VideoDto;
import com.travelvcommerce.contentslaveservice.entity.Video;
import com.travelvcommerce.contentslaveservice.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaVideoInfoConsumerServiceImpl implements KafkaVideoInfoConsumerService {
    private final ObjectMapper objectMapper;
    private final VideoRepository videoRepository;

    @Override
    @KafkaListener(topics = "video-create")
    public void createVideo(String payload) {
        log.info("received payload='{}'", payload);

        VideoDto videoDto;
        try {
            videoDto = objectMapper.readValue(payload, VideoDto.class);
        } catch (Exception e) {
            log.error("Error converting payload to video", e);
            return;
        }

        try {
            videoRepository.save(videoDto.toEntity());
        } catch (Exception e) {
            log.error("Error saving video", e);
        }
    }

    @Override
    @KafkaListener(topics = "video-update")
    public void updateVideo(String payload) {
        log.info("received payload='{}'", payload);

        VideoDto videoDto;
        try {
            videoDto = objectMapper.readValue(payload, VideoDto.class);
        } catch (Exception e) {
            log.error("Error converting payload to video", e);
            return;
        }

        String videoId = videoDto.getVideoId();

        try {
            Video video = videoRepository.findByByVideoId(videoId).orElseThrow(() -> new RuntimeException("video not found"));

            video.update(videoDto);

            videoRepository.save(video);
        } catch (Exception e) {
            log.error("Error updating video", e);
        }
    }

    @Override
    @KafkaListener(topics = "video-delete")
    public void deleteVideo(String payload) {
        log.info("received payload='{}'", payload);

        String videoId = payload;

        try {
            videoRepository.findByByVideoId(videoId).ifPresent(video -> videoRepository.delete(video));
        } catch (Exception e) {
            log.error("Error deleting video", e);
        }
    }
}
