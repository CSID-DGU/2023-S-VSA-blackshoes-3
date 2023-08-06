package com.travelvcommerce.uploadservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelvcommerce.uploadservice.dto.DenormalizedVideoDto;
import com.travelvcommerce.uploadservice.repository.VideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Primary
public class KafkaVideoProducerServiceImpl implements KafkaVideoProducerService {
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private KafkaProducer kafkaProducer;

    @Transactional
    @Override
    public void postDenormalizeData(DenormalizedVideoDto denormalizedVideoDto) {
        String topic = "video-create";

        String videoJsonString;
        try {
            videoJsonString = objectMapper.writeValueAsString(denormalizedVideoDto);
        } catch (JsonProcessingException e) {
            log.error("Post denormalize data error", e);
            throw new RuntimeException("Post denormalize data error");
        }

        kafkaProducer.send(topic, videoJsonString);
    }

    @Transactional
    @Override
    public void putDenormalizeData(DenormalizedVideoDto denormalizedVideoDto) {
        String topic = "video-update";

        String videoJsonString;
        try {
            videoJsonString = objectMapper.writeValueAsString(denormalizedVideoDto);
        } catch (JsonProcessingException e) {
            log.error("Post denormalize data error", e);
            throw new RuntimeException("Post denormalize data error");
        }

        kafkaProducer.send(topic, videoJsonString);
    }

    @Override
    public void deleteDenormalizeData(String videoId) {
        String topic = "video-delete";

        kafkaProducer.send(topic, videoId);
    }
}
