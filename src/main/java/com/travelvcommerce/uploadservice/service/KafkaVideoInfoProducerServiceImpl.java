package com.travelvcommerce.uploadservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelvcommerce.uploadservice.dto.DenormalizedVideoDto;
import com.travelvcommerce.uploadservice.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Primary
@RequiredArgsConstructor
public class KafkaVideoInfoProducerServiceImpl implements KafkaVideoInfoProducerService {
    private final ObjectMapper objectMapper;
    private final KafkaProducer kafkaProducer;

    @Transactional
    @Override
    public void createVideo(DenormalizedVideoDto denormalizedVideoDto) {
        String topic = "video-create";

        String videoJsonString;
        try {
            videoJsonString = objectMapper.writeValueAsString(denormalizedVideoDto);
        } catch (JsonProcessingException e) {
            log.error("Mapping dto to json string error", e);
            throw new RuntimeException("Error publishing created video");
        }

        kafkaProducer.send(topic, videoJsonString);
    }

    @Transactional
    @Override
    public void updateVideo(DenormalizedVideoDto denormalizedVideoDto) {
        String topic = "video-update";

        String videoJsonString;
        try {
            videoJsonString = objectMapper.writeValueAsString(denormalizedVideoDto);
        } catch (JsonProcessingException e) {
            log.error("Mapping dto to json string error", e);
            throw new RuntimeException("Error publishing updated video");
        }

        kafkaProducer.send(topic, videoJsonString);
    }

    @Override
    public void deleteVideo(String videoId) {
        String topic = "video-delete";

        kafkaProducer.send(topic, videoId);
    }
}
