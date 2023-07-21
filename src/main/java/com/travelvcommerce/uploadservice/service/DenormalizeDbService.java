package com.travelvcommerce.uploadservice.service;

import com.travelvcommerce.uploadservice.dto.DenormalizedVideoDto;

public interface DenormalizeDbService {
    DenormalizedVideoDto denormalizeDb(String userId, String videoId);
    void postDenormalizeData(String userId, String videoId);
    void putDenormalizeData(String userId, String videoId);
    void deleteDenormalizeData(String userId, String videoId);
}
