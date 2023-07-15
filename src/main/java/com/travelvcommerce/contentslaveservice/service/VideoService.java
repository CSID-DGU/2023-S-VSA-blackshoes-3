package com.travelvcommerce.contentslaveservice.service;

import com.travelvcommerce.contentslaveservice.dto.VideoDto;
import org.springframework.data.domain.Page;

public interface VideoService {
    Page<VideoDto.VideoListResponseDto> getVideos(String q, int page, int size);
    VideoDto.VideoDetailResponseDto getVideo(String videoId);
}
