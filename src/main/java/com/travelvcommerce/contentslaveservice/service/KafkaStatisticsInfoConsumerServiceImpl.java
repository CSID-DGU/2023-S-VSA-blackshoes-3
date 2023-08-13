package com.travelvcommerce.contentslaveservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelvcommerce.contentslaveservice.dto.StatisticsInfoDto;
import com.travelvcommerce.contentslaveservice.entity.Video;
import com.travelvcommerce.contentslaveservice.repository.VideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaStatisticsInfoConsumerServiceImpl implements KafkaStatisticsInfoConsumerService {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private VideoRepository videoRepository;

    @Override
    public void updateStatistics(String payload) {
        log.info("received payload='{}'", payload);
        StatisticsInfoDto statisticsInfoDto;

        try {
            statisticsInfoDto = objectMapper.readValue(payload, StatisticsInfoDto.class);
        } catch (Exception e) {
            log.error("Error converting payload to statisticsInfo", e);
            return;
        }

        String videoId = statisticsInfoDto.getVideoId();
        Video video = videoRepository.findByByVideoId(videoId).orElseThrow(() -> new RuntimeException("video not found"));

        if (statisticsInfoDto.getLikes() != 0) {
            video.updateLikes(statisticsInfoDto.getLikes());
            return;
        }
        if (statisticsInfoDto.getViews() != 0) {
            video.updateViews(statisticsInfoDto.getViews());
            return;
        }
        if (statisticsInfoDto.getAdClicks() != 0) {
            video.updateAdClicks(statisticsInfoDto.getAdClicks());
            return;
        }
    }
}
