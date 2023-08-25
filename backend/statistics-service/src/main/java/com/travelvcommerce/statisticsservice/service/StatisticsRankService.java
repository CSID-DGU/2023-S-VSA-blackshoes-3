package com.travelvcommerce.statisticsservice.service;

import com.travelvcommerce.statisticsservice.dto.RankDto;
import com.travelvcommerce.statisticsservice.dto.RankResponseDto;

import java.util.List;

public interface StatisticsRankService {
    RankResponseDto.VideoViewRankResponseDto getVideoViewRank(String sellerId, int size, boolean refresh);
    RankResponseDto.TagViewRankResponseDto getTagViewRank(String sellerId, int size, boolean refresh);
    RankResponseDto.VideoLikeRankResponseDto getVideoLikeRank(String sellerId, int size, boolean refresh);
    RankResponseDto.VideoAdClickRankResponseDto getAdClickRank(String sellerId, int size, boolean refresh);
}
