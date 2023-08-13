package com.travelvcommerce.statisticsservice.service;

import com.travelvcommerce.statisticsservice.dto.RankDto;

import java.util.List;

public interface StatisticsRankService {
    List<RankDto.VideoViewRankDto> getVideoViewTop10(String sellerId);
    List<RankDto.TagViewRankDto> getTagViewTop10(String sellerId);
    List<RankDto.VideoLikeRankDto> getVideoLikeTop10(String sellerId);
    List<RankDto.VideoAdClickRankDto> getAdClickTop10(String sellerId);
}
