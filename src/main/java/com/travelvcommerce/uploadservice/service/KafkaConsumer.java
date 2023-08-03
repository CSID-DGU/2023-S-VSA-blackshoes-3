package com.travelvcommerce.uploadservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelvcommerce.uploadservice.entity.Uploader;
import com.travelvcommerce.uploadservice.entity.Video;
import com.travelvcommerce.uploadservice.repository.UploaderRepository;
import com.travelvcommerce.uploadservice.repository.VideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class KafkaConsumer {
    @Autowired
    private UploaderRepository uploaderRepository;
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ModelMapper modelMapper;

    @KafkaListener(topics = "uploader-create")
    public void createUploader(String payload) {
        log.info("received payload='{}'", payload);

        Map<Object, Object> payloadMap;
        try {
            payloadMap = objectMapper.convertValue(payload, HashMap.class);
        } catch (Exception e) {
            log.error("Error converting payload to map", e);
            return;
        }

        Uploader uploader;
        try {
            uploader = modelMapper.map(payloadMap, Uploader.class);
        } catch (Exception e) {
            log.error("Error mapping payload to uploader", e);
            return;
        }

        try {
            uploaderRepository.save(uploader);
        } catch (Exception e) {
            log.error("Error saving uploader", e);
        }
    }

    @KafkaListener(topics = "uploader-update")
    public void updateUploader(String payload) {
        log.info("received payload='{}'", payload);

        Map<Object, Object> payloadMap;
        try {
            payloadMap = objectMapper.convertValue(payload, HashMap.class);
        } catch (Exception e) {
            log.error("Error converting payload to map", e);
            return;
        }

        String sellerId = (String) payloadMap.get("sellerId");
        Uploader uploader;
        try {
            uploader = uploaderRepository.findBySellerId(sellerId).orElseThrow(() -> new NoSuchElementException("uploader not found"));
        } catch (Exception e) {
            log.error("Uploader not found", e);
            return;
        }

        try {
            Uploader.update(uploader, payloadMap);
        } catch (Exception e) {
            log.error("Error updating uploader", e);
        }
    }

    @KafkaListener(topics = "uploader-delete")
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

        try {
            uploaderRepository.delete(uploader);
        } catch (Exception e) {
            log.error("Error deleting uploader", e);
        }
    }
}
