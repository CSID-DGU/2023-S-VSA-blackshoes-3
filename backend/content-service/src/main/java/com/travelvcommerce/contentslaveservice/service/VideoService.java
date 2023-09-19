package com.travelvcommerce.contentslaveservice.service;

import com.travelvcommerce.contentslaveservice.dto.VideoDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface VideoService {
    VideoDto.VideoDetailResponseDto getVideoById(String idType, String id);

    Page<VideoDto.VideoListResponseDto> getVideos(String sortType, int page, int size);

    Page<VideoDto.VideoListResponseDto> getVideosBySellerId(String sellerId, String q, int page, int size);

    Page<VideoDto.VideoListResponseDto> getVideosByIdList(String idType, List<String> idList, String sortType, int page, int size);

    // 비디오 검색
    Page<VideoDto.VideoListResponseDto> searchVideos(String type, String query, String sortType, int page, int size);

    Page<VideoDto.VideoListResponseDto> getVideosByTagId(String q, String sortType, int page, int size);

    List<VideoDto.VideoListResponseDto> getVideosByVideoIdList(List<String> videoIdList);

    List<VideoDto.VideoListResponseDto> getRandomVideosByTagIdList(List<String> tagIdList, String userId, int page);
}
