package com.travelvcommerce.statisticsservice.service;

import com.travelvcommerce.statisticsservice.dto.count.VideoCountInfoDto;

public interface KafkaVideoInfoProducerService {
    void updateVideoStatistics(VideoCountInfoDto videoCountInfoDto);
}
