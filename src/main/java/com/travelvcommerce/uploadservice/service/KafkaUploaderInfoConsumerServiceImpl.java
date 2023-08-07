package com.travelvcommerce.uploadservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelvcommerce.uploadservice.dto.UploaderDto;
import com.travelvcommerce.uploadservice.entity.Uploader;
import com.travelvcommerce.uploadservice.entity.Video;
import com.travelvcommerce.uploadservice.repository.UploaderRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
public class KafkaUploaderInfoConsumerServiceImpl implements KafkaUploaderInfoConsumerService {
    @Autowired
    private UploaderRepository uploaderRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private KafkaVideoInfoProducerService kafkaVideoInfoProducerService;

    @Override
    @KafkaListener(topics = "uploader-create")
    @Transactional
    public void createUploader(String payload) {
        log.info("received payload='{}'", payload);

        UploaderDto uploaderDto;
        try {
            uploaderDto = objectMapper.readValue(payload, UploaderDto.class);
        } catch (Exception e) {
            log.error("Error converting payload to uploader dto", e);
            return;
        }

        Uploader uploader;
        try {
            uploader = modelMapper.map(uploaderDto, Uploader.class);
        } catch (Exception e) {
            log.error("Error mapping uploader dto to uploader", e);
            return;
        }

        try {
            uploaderRepository.save(uploader);
        } catch (Exception e) {
            log.error("Error saving uploader", e);
        }
    }

    @Override
    @KafkaListener(topics = "uploader-update")
    @Transactional
    public void updateUploader(String payload) {
        log.info("received payload='{}'", payload);

        UploaderDto uploaderDto;
        try {
            uploaderDto = objectMapper.readValue(payload, UploaderDto.class);
        } catch (Exception e) {
            log.error("Error converting payload to uploader dto", e);
            return;
        }

        String sellerId = uploaderDto.getSellerId();
        Uploader uploader;
        try {
            uploader = uploaderRepository.findBySellerId(sellerId).orElseThrow(() -> new NoSuchElementException("uploader not found"));
        } catch (Exception e) {
            log.error("Uploader not found", e);
            return;
        }

        try {
            Uploader.update(uploader, uploaderDto);
        } catch (Exception e) {
            log.error("Error updating uploader", e);
        }
    }

    @Override
    @KafkaListener(topics = "uploader-delete")
    @Transactional
    public void deleteUploader(String payload) {
        log.info("received payload='{}'", payload);

        String sellerId = payload;

        Uploader uploader;
        try {
            uploader = uploaderRepository.findBySellerId(sellerId).orElseThrow(() -> new NoSuchElementException("uploader not found"));
        } catch (Exception e) {
            log.error("Uploader not found", e);
            return;
        }

        List<String> uploaderVideoIds = uploader.getVideos().stream().map(Video::getVideoId).collect(Collectors.toList());

        uploaderVideoIds.forEach(videoId -> {kafkaVideoInfoProducerService.deleteVideo(videoId);});

        try {
            uploaderRepository.delete(uploader);
        } catch (Exception e) {
            log.error("Error deleting uploader", e);
        }
    }
}
