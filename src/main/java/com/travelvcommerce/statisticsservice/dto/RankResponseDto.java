package com.travelvcommerce.statisticsservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RankResponseDto {
    private List<RankDto.VideoViewRankDto> videoViewRank;
    private List<RankDto.TagViewRankDto> tagViewRank;
    private List<RankDto.VideoLikeRankDto> videoLikeRank;
    private List<RankDto.VideoAdClickRankDto> videoAdClickRank;
}
