package com.travelvcommerce.statisticsservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelvcommerce.statisticsservice.dto.count.VideoCountInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaVideoInfoProducerServiceImpl implements KafkaVideoInfoProducerService {
    private final ObjectMapper objectMapper;
    private final KafkaProducer kafkaProducer;

    @Override
    public void updateVideoStatistics(VideoCountInfoDto videoCountInfoDto) {
        String topic = "statistics-update";

        String videoJsonString;
        try {
            videoJsonString = objectMapper.writeValueAsString(videoCountInfoDto);
        } catch (JsonProcessingException e) {
            log.error("Mapping dto to json string error", e);
            throw new RuntimeException("Error publishing created video");
        }

        kafkaProducer.send(topic, videoJsonString);
    }
}
