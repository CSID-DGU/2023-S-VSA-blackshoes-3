package com.travelvcommerce.statisticsservice.service;

import com.travelvcommerce.statisticsservice.dto.VideoCountInfoDto;

public interface StatisticsUpdateService {
    VideoCountInfoDto increaseViewCount(String videoId, String userId);

    VideoCountInfoDto increaseVideoLikeCount(String videoId, String userId);

    VideoCountInfoDto decreaseVideoLikeCount(String videoId, String userId);

    VideoCountInfoDto increaseVideoAdClickCount(String adId, String userId);
}
