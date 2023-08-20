package com.travelvcommerce.contentslaveservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelvcommerce.contentslaveservice.dto.StatisticsInfoDto;
import com.travelvcommerce.contentslaveservice.entity.Video;
import com.travelvcommerce.contentslaveservice.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaStatisticsInfoConsumerServiceImpl implements KafkaStatisticsInfoConsumerService {
    private final ObjectMapper objectMapper;
    private final VideoRepository videoRepository;

    @Override
    @KafkaListener(topics = "statistics-update")
    @Transactional
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

        if (statisticsInfoDto.getLikes() != -1) {
            video.updateLikes(statisticsInfoDto.getLikes());
            videoRepository.save(video);
            return;
        }
        if (statisticsInfoDto.getViews() != -1) {
            video.updateViews(statisticsInfoDto.getViews());
            videoRepository.save(video);
            return;
        }
        if (statisticsInfoDto.getAdClicks() != -1) {
            video.updateAdClicks(statisticsInfoDto.getAdClicks());
            videoRepository.save(video);
            return;
        }
    }
}
