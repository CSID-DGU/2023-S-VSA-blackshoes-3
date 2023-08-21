package com.travelvcommerce.uploadservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelvcommerce.uploadservice.dto.DenormalizedVideoDto;
import com.travelvcommerce.uploadservice.dto.UploaderDto;
import com.travelvcommerce.uploadservice.entity.Uploader;
import com.travelvcommerce.uploadservice.entity.Video;
import com.travelvcommerce.uploadservice.repository.UploaderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaUploaderInfoConsumerServiceImpl implements KafkaUploaderInfoConsumerService {
    private final UploaderRepository uploaderRepository;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    private final KafkaVideoInfoProducerService kafkaVideoInfoProducerService;

    @Override
    @KafkaListener(topics = "uploader-create")
    @Transactional
    public void createUploader(String payload, Acknowledgment acknowledgment) {
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

        acknowledgment.acknowledge();
    }

    @Override
    @KafkaListener(topics = "uploader-update")
    @Transactional
    public void updateUploader(String payload, Acknowledgment acknowledgment) {
        log.info("received payload='{}'", payload);

        UploaderDto.UploaderModifyRequestDto uploaderModifyRequestDto;
        try {
            uploaderModifyRequestDto = objectMapper.readValue(payload, UploaderDto.UploaderModifyRequestDto.class);
        } catch (Exception e) {
            log.error("Error converting payload to uploader dto", e);
            return;
        }

        String sellerId = uploaderModifyRequestDto.getSellerId();
        Uploader uploader;
        try {
            uploader = uploaderRepository.findBySellerId(sellerId).orElseThrow(() -> new NoSuchElementException("uploader not found"));
        } catch (Exception e) {
            log.error("Uploader not found", e);
            return;
        }

        try {
            uploader.update(uploaderModifyRequestDto);
        } catch (Exception e) {
            log.error("Error updating uploader", e);
        }

        List<Video> uploaderVideos = uploader.getVideos();

        try {
            uploaderVideos.stream().forEach(
                    video -> {
                        DenormalizedVideoDto denormalizedVideoDto = DenormalizedVideoDto.builder()
                                .videoId(video.getVideoId())
                                .sellerId(uploader.getSellerId())
                                .sellerLogo(Base64.getEncoder().encodeToString(uploader.getSellerLogo()))
                                .sellerName(uploader.getSellerName())
                                .build();
                        kafkaVideoInfoProducerService.updateVideo(denormalizedVideoDto);
                    }
            );
        } catch (Exception e) {
            log.error("Error publishing updated video", e);
        }

        acknowledgment.acknowledge();
    }

    @Override
    @KafkaListener(topics = "uploader-delete")
    @Transactional
    public void deleteUploader(String payload, Acknowledgment acknowledgment) {
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

        acknowledgment.acknowledge();
    }
}
