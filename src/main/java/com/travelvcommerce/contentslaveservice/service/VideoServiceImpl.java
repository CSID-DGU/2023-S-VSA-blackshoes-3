package com.travelvcommerce.contentslaveservice.service;

import com.travelvcommerce.contentslaveservice.dto.VideoDto;
import com.travelvcommerce.contentslaveservice.repository.VideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VideoServiceImpl implements VideoService {
    @Autowired
    private VideoRepository videoRepository;

    @Override
    public Page<VideoDto.VideoListResponseDto> getVideos(String q, int page, int size) {
        Sort sortBy = Sort.by(Sort.Direction.DESC, q);
        Pageable pageable = PageRequest.of(page, size, sortBy);
        return videoRepository.findVideosWithSelectedFields(pageable);
    }

    @Override
    public VideoDto.VideoDetailResponseDto getVideo(String videoId) {
        return videoRepository.findByVideoId(videoId);
    }
}
