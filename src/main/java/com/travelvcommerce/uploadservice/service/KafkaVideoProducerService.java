package com.travelvcommerce.uploadservice.service;

import com.travelvcommerce.uploadservice.dto.DenormalizedVideoDto;

public interface KafkaVideoProducerService {
    void postDenormalizeData(DenormalizedVideoDto denormalizedVideoDto);
    void putDenormalizeData(DenormalizedVideoDto denormalizedVideoDto);
    void deleteDenormalizeData(String videoId);
}
