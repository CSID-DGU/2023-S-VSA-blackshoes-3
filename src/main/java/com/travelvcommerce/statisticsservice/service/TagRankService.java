package com.travelvcommerce.statisticsservice.service;

import com.travelvcommerce.statisticsservice.dto.RankResponseDto;

public interface TagRankService {
    RankResponseDto.TagRankResponseDto getTagRankByRegion();
    RankResponseDto.TagRankResponseDto getTagRankByTheme();
}
