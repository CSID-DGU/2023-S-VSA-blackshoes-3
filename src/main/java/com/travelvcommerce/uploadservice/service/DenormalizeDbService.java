package com.travelvcommerce.uploadservice.service;

import com.travelvcommerce.uploadservice.dto.DenormalizedVideoDto;

public interface DenormalizeDbService {
    DenormalizedVideoDto denormalizeDb(String userId, String videoId);
    void postDenormalizeData(DenormalizedVideoDto denormalizedVideoDto);
    void putDenormalizeData(String videoId, DenormalizedVideoDto denormalizedVideoDto);
    void deleteDenormalizeData(String videoId, DenormalizedVideoDto denormalizedVideoDto);
}
