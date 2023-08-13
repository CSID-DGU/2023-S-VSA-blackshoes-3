package com.travelvcommerce.statisticsservice.service;

import com.travelvcommerce.statisticsservice.dto.VideoCountInfoDto;

public interface StatisticsUpdateService {
    VideoCountInfoDto increaseVideoViewCount(String videoId, String userId);

    void increaseTagViewCount(String videoId, String tagId, String userId);

    VideoCountInfoDto increaseVideoLikeCount(String videoId, String userId);

    VideoCountInfoDto decreaseVideoLikeCount(String videoId, String userId);

    VideoCountInfoDto increaseVideoAdClickCount(String adId, String userId);
}
