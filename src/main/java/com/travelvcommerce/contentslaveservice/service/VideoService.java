package com.travelvcommerce.contentslaveservice.service;

import com.travelvcommerce.contentslaveservice.dto.VideoDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface VideoService {
    VideoDto.VideoDetailResponseDto getVideoById(String idType, String id);

    Page<VideoDto.VideoListResponseDto> getVideos(String q, int page, int size);

    Page<VideoDto.VideoListResponseDto> getVideosBySellerId(String sellerId, String q, int page, int size);

    Page<VideoDto.VideoListResponseDto> getVideosByIdList(String idType, List<String> idList, int page, int size);
}
