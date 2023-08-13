package com.travelvcommerce.statisticsservice.service;

import com.travelvcommerce.statisticsservice.dto.RankDto;
import com.travelvcommerce.statisticsservice.dto.RankResponseDto;

import java.util.List;

public interface StatisticsRankService {
    RankResponseDto.VideoViewRankResponseDto getVideoViewTop10(String sellerId);
    RankResponseDto.TagViewRankResponseDto getTagViewTop10(String sellerId);
    RankResponseDto.VideoLikeRankResponseDto getVideoLikeTop10(String sellerId);
    RankResponseDto.VideoAdClickRankResponseDto getAdClickTop10(String sellerId);
}
