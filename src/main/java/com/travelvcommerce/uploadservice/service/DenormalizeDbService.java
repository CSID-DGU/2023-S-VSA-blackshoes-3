package com.travelvcommerce.uploadservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.travelvcommerce.uploadservice.vo.UpdatedField;

public interface DenormalizeDbService {
    void postDenormalizeData(String videoId);
    void putDenormalizeData(String videoId, UpdatedField updatedField);
    void deleteDenormalizeData(String videoId);
}
