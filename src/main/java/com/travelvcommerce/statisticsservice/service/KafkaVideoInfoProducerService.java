package com.travelvcommerce.statisticsservice.service;

import com.travelvcommerce.statisticsservice.dto.VideoCountInfoDto;

public interface KafkaVideoInfoProducerService {
    void updateVideoStatistics(VideoCountInfoDto videoCountInfoDto);
}
