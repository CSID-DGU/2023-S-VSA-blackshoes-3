package com.travelvcommerce.userservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelvcommerce.userservice.dto.SellerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaUploaderInfoProducerServiceImpl implements KafkaUploaderInfoProducerService {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    KafkaProducer kafkaProducer;

    @Override
    public void createUploader(SellerDto.SellerInfoDto uploaderInfoDto) {
        String topic = "uploader-create";

        String sellerJsonString;
        try {
            sellerJsonString = objectMapper.writeValueAsString(uploaderInfoDto);
        } catch (Exception e) {
            log.error("Mapping dto to json string error", e);
            throw new RuntimeException("Error publishing created uploader");
        }

        kafkaProducer.send(topic, sellerJsonString);
    }

    @Override
    public void updateUploader(SellerDto.SellerInfoDto uploaderInfoDto) {
        String topic = "uploader-update";

        String sellerJsonString;
        try {
            sellerJsonString = objectMapper.writeValueAsString(uploaderInfoDto);
        } catch (Exception e) {
            log.error("Mapping dto to json string error", e);
            throw new RuntimeException("Error publishing updated uploader");
        }

        kafkaProducer.send(topic, sellerJsonString);
    }

    @Override
    public void deleteUploader(String uploaderId) {
        String topic = "uploader-delete";

        kafkaProducer.send(topic, uploaderId);
    }
}
