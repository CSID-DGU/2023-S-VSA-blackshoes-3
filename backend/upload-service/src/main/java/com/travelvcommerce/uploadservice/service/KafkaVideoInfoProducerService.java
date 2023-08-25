package com.travelvcommerce.uploadservice.service;

import com.travelvcommerce.uploadservice.dto.DenormalizedVideoDto;

public interface KafkaVideoInfoProducerService {
    void createVideo(DenormalizedVideoDto denormalizedVideoDto);
    void updateVideo(DenormalizedVideoDto denormalizedVideoDto);
    void deleteVideo(String videoId);
}
