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

import java.util.List;

@Service
@Slf4j
public class VideoServiceImpl implements VideoService {
    @Autowired
    private VideoRepository videoRepository;

    // 전체 영상 조회, 정렬 정보 q로 받아서 정렬, 페이징 처리
    @Override
    public Page<VideoDto.VideoListResponseDto> getVideos(String q, int page, int size) {
        Sort sortBy = Sort.by(Sort.Direction.DESC, q);
        Pageable pageable = PageRequest.of(page, size, sortBy);
        Page<VideoDto.VideoListResponseDto> videos = videoRepository.findVideosWithSelectedFields(pageable);
        return videos;
    }

    // 판매자별 영상 조회, 정렬 정보 q로 받아서 정렬, 페이징 처리
    @Override
    public Page<VideoDto.VideoListResponseDto> getVideosBySellerId(String sellerId, String q, int page, int size) {
        Sort sortBy = Sort.by(Sort.Direction.DESC, q);
        Pageable pageable = PageRequest.of(page, size, sortBy);
        Page<VideoDto.VideoListResponseDto> videos = videoRepository.findVideosWithSellerIdAndSelectedFields(sellerId, pageable);
        return videos;
    }

    // idType을 key로 갖는 value가 idData에 포함되는 영상 조회, 정렬 정보 q로 받아서 정렬, 페이징 처리
    @Override
    public Page<VideoDto.VideoListResponseDto> getVideosByIdList(String idType, List<String> idData, int page, int size) {
        Sort sortBy = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sortBy);
        Page<VideoDto.VideoListResponseDto> videos =
                videoRepository.findVideosWithIdListAndSelectedFields(idType, idData, pageable);
        return videos;
    }

    // 개별 영상 조회
    @Override
    public VideoDto.VideoDetailResponseDto getVideo(String videoId) {
        VideoDto.VideoDetailResponseDto video =
                videoRepository.findByVideoId(videoId)
                        .orElseThrow(() -> new RuntimeException("No video found"));
        return video;
    }
}
