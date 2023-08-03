package com.travelvcommerce.contentslaveservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelvcommerce.contentslaveservice.dto.VideoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumer {
    @Autowired
    private DatabaseService databaseService;
    @Autowired
    private ObjectMapper objectMapper;

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
            databaseService.create(videoDto);
        } catch (Exception e) {
            log.error("Error saving video", e);
        }
    }

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

        try {
            databaseService.update(videoDto.getVideoId(), videoDto);
        } catch (Exception e) {
            log.error("Error updating video", e);
        }
    }

    @KafkaListener(topics = "video-delete")
    public void deleteVideo(String payload) {
        log.info("received payload='{}'", payload);

        String videoId = payload;

        try {
            databaseService.delete(videoId);
        } catch (Exception e) {
            log.error("Error deleting video", e);
        }
    }
}
