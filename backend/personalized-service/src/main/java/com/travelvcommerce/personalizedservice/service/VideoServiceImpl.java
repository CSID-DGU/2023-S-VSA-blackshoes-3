package com.travelvcommerce.personalizedservice.service;

import com.travelvcommerce.personalizedservice.dto.VideoDto;
import com.travelvcommerce.personalizedservice.entity.LikeVideo;
import com.travelvcommerce.personalizedservice.entity.ViewVideo;
import com.travelvcommerce.personalizedservice.repository.LikeVideoRepository;
import com.travelvcommerce.personalizedservice.repository.ViewVideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@RequiredArgsConstructor
@Service
public class VideoServiceImpl implements VideoService {
    private final ViewVideoRepository viewVideoRepository;
    private final LikeVideoRepository likeVideoRepository;

    @Transactional
    @Override
    public Map<String, String> viewVideo(String userId, VideoDto.ViewVideoRequestDto viewVideoRequestDto) {
        VideoDto.ViewVideoResponseDto viewVideoResponseDto = new VideoDto.ViewVideoResponseDto();
        String videoId = viewVideoRequestDto.getVideoId();
        String sellerId = viewVideoRequestDto.getSellerId();

        if (viewVideoRepository.existsByUserIdAndVideoId(userId, videoId)) {
            ViewVideo viewVideo = viewVideoRepository.findByUserIdAndVideoId(userId, videoId);

            viewVideo.increaseViewCount();
            viewVideo.setUpdatedAt(LocalDateTime.now());

            viewVideoResponseDto.setVideoViewCount(viewVideo.getVideoViewCount());
            viewVideoResponseDto.setCreatedAt(viewVideo.getCreatedAt());
        } else {
            ViewVideo viewVideo = ViewVideo.builder()
                    .userId(userId)
                    .videoId(videoId)
                    .sellerId(sellerId)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .videoViewCount(1L)
                    .build();
            viewVideoRepository.save(viewVideo);

            viewVideoResponseDto.setVideoViewCount(viewVideo.getVideoViewCount());
            viewVideoResponseDto.setCreatedAt(viewVideo.getCreatedAt());
        }

        viewVideoResponseDto.setUserId(userId);
        viewVideoResponseDto.setUpdatedAt(LocalDateTime.now());


        Map<String, String> viewVideoResponse = new HashMap<>();
        viewVideoResponse.put("userId", userId);
        viewVideoResponse.put("videoId", videoId);
        viewVideoResponse.put("createdAt", viewVideoResponseDto.getFormattedCreatedAt());
        viewVideoResponse.put("updatedAt", viewVideoResponseDto.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")));

        return viewVideoResponse;
    }
    @Transactional
    @Override
    public void unviewVideo(String userId, String videoId) {
        if (!viewVideoRepository.existsByUserIdAndVideoId(userId, videoId)) {
            throw new ResourceNotFoundException("Invalid video id or user id");
        }
        viewVideoRepository.deleteByUserIdAndVideoId(userId, videoId);

    }

    @Transactional
    @Override
    public Map<String, String> likeVideo(String userId, VideoDto.LikeVideoRequestDto likeVideoRequestDto) {
        String videoId = likeVideoRequestDto.getVideoId();
        String sellerId = likeVideoRequestDto.getSellerId();

        if(likeVideoRepository.existsByUserIdAndVideoId(userId, videoId)){
            throw new CustomBadRequestException("이미 좋아요를 누른 동영상입니다.");
        }

        likeVideoRepository.save(LikeVideo.builder()
                .userId(userId)
                .videoId(videoId)
                .sellerId(sellerId)
                .createdAt(LocalDateTime.now())
                .build());

        VideoDto.LikeVideoResponseDto likeVideoResponseDto = new VideoDto.LikeVideoResponseDto();
        likeVideoResponseDto.setCreatedAt(LocalDateTime.now());

        Map<String, String> likeVideoResponse = new HashMap<>();

        likeVideoResponse.put("userId", userId);
        likeVideoResponse.put("videoId", videoId);
        likeVideoResponse.put("createdAt",likeVideoResponseDto.getFormattedCreatedAt());

        return likeVideoResponse;
    }

    @Transactional
    @Override
    public void unlikeVideo(String userId, String videoId) {
        if (!likeVideoRepository.existsByUserIdAndVideoId(userId, videoId)) {
            throw new ResourceNotFoundException("Invalid video id or user id");
        }
        likeVideoRepository.deleteByUserIdAndVideoId(userId, videoId);
    }

    @Override
    public Page<String> getLikedVideoIdList(String userId, int page, int size){
        if (!likeVideoRepository.existsByUserId(userId)) {
            throw new ResourceNotFoundException("Invalid user id");
        }
        Sort sortBy = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sortBy);
        Page<LikeVideo> pagedLikeVideos = likeVideoRepository.findByUserId(userId, pageable);

        return pagedLikeVideos.map(LikeVideo::getVideoId);
    }
    @Override
    public Page<String> getViewVideoIdListWithViewCount(String userId, int page, int size) {
        if (!viewVideoRepository.existsByUserId(userId)) {
            throw new ResourceNotFoundException("Invalid user id");
        }
        Sort sortBy = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sortBy);

        Page<ViewVideo> pagedViewVideos = viewVideoRepository.findByUserId(userId, pageable);

        return pagedViewVideos.map(ViewVideo::getVideoId);
    }

    @Override
    public Boolean isUserLikedVideo(String userId, String videoId) {
        if (!likeVideoRepository.existsByUserIdAndVideoId(userId, videoId)) {
            return false;
        }
        return true;
    }
}
