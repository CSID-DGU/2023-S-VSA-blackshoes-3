package com.travelvcommerce.statisticsservice.service;

import com.travelvcommerce.statisticsservice.dto.count.VideoCountInfoDto;
import com.travelvcommerce.statisticsservice.entity.*;
import com.travelvcommerce.statisticsservice.exception.UserAlreadyClickedAdException;
import com.travelvcommerce.statisticsservice.exception.UserAlreadyLikedVideoException;
import com.travelvcommerce.statisticsservice.exception.UserAlreadyViewedVideoException;
import com.travelvcommerce.statisticsservice.exception.UserDidNotLikedVideoException;
import com.travelvcommerce.statisticsservice.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticsUpdateServiceImpl implements StatisticsUpdateService {
    private final RedisTemplate<String, String> redisTemplate;
    private final VideoRepository videoRepository;
    private final VideoLikeCountRepository videoLikeCountRepository;
    private final LikeRepository likeRepository;
    private final AdClickCountRepository adClickCountRepository;

    @Override
    @Transactional
    public VideoCountInfoDto increaseViewCount(String videoId, String userId) {
        String viewCountKey = "viewCount:" + videoId + ":" + userId;
        if (redisTemplate.hasKey(viewCountKey)) {
            throw new UserAlreadyViewedVideoException("User already viewed video");
        }


        Video video = videoRepository.findByVideoId(videoId).orElseThrow(() -> new NoSuchElementException("Video not found"));

        VideoViewCount videoViewCount = video.getVideoViewCount();
        try {
            videoViewCount.increaseViewCount();
        } catch (RuntimeException e) {
            log.error("Error increasing video view count", e);
            throw new RuntimeException(e.getMessage());
        }

        try {
            video.getTagViewCounts().stream().forEach(tagViewCount -> {
                tagViewCount.increaseViewCount();
            });
        } catch (RuntimeException e) {
            log.error("Error increasing tag view count", e);
            throw new RuntimeException(e.getMessage());
        }

        VideoCountInfoDto videoCountInfoDto = VideoCountInfoDto.builder()
                .videoId(videoId)
                .views(videoViewCount.getViewCount())
                .likes(-1L)
                .adClicks(-1L)
                .build();

        redisTemplate.opsForValue().set(viewCountKey, "true");
        redisTemplate.expire(viewCountKey, 60 * 60 * 24, TimeUnit.SECONDS);

        return videoCountInfoDto;
    }

    @Override
    @Transactional
    public VideoCountInfoDto increaseVideoLikeCount(String videoId, String userId) {
        if (likeRepository.findByVideoIdAndUserId(videoId, userId).isPresent()) {
            throw new UserAlreadyLikedVideoException("User already liked video");
        }

        Video video = videoRepository.findByVideoId(videoId).orElseThrow(() -> new NoSuchElementException("Video not found"));
        VideoLikeCount videoLikeCount = video.getVideoLikeCount();

        try {
            videoLikeCount.increaseLikeCount();
        } catch (RuntimeException e) {
            log.error("Error increasing video like count", e);
            throw new RuntimeException(e.getMessage());
        }

        try {
            Like like = Like.builder()
                    .video(video)
                    .userId(userId)
                    .build();
            likeRepository.save(like);
        } catch (RuntimeException e) {
            log.error("Error saving like", e);
            throw new RuntimeException(e.getMessage());
        }

        VideoCountInfoDto videoCountInfoDto = VideoCountInfoDto.builder()
                .videoId(videoId)
                .views(-1L)
                .likes(videoLikeCount.getLikeCount())
                .adClicks(-1L)
                .build();

        return videoCountInfoDto;
    }

    @Override
    @Transactional
    public VideoCountInfoDto decreaseVideoLikeCount(String videoId, String userId) {
        Like like = likeRepository.findByVideoIdAndUserId(videoId, userId).orElseThrow(() -> new UserDidNotLikedVideoException("User did not liked video"));

        VideoLikeCount videoLikeCount = videoLikeCountRepository.findByVideoId(videoId).orElseThrow(() -> new NoSuchElementException("Video like count not found"));

        try {
            videoLikeCount.decreaseLikeCount();
        } catch (RuntimeException e) {
            log.error("Error decreasing video like count", e);
            throw new RuntimeException(e.getMessage());
        }

        try {
            likeRepository.delete(like);
        } catch (RuntimeException e) {
            log.error("Error deleting like", e);
            throw new RuntimeException(e.getMessage());
        }

        VideoCountInfoDto videoCountInfoDto = VideoCountInfoDto.builder()
                .videoId(videoId)
                .views(-1L)
                .likes(videoLikeCount.getLikeCount())
                .adClicks(-1L)
                .build();

        return videoCountInfoDto;
    }

    @Override
    @Transactional
    public VideoCountInfoDto increaseVideoAdClickCount(String adId, String userId) {
        String adClickCountKey = "adClickCount:" + adId + ":" + userId;
        if (redisTemplate.hasKey(adClickCountKey)) {
            throw new UserAlreadyClickedAdException("User already clicked ad");
        }

        AdClickCount adClickCount = adClickCountRepository.findByAdId(adId).orElseThrow(() -> new NoSuchElementException("Ad click count not found"));

        try {
            adClickCount.increaseClickCount();
        } catch (RuntimeException e) {
            log.error("Error increasing ad click count", e);
            throw new RuntimeException(e.getMessage());
        }

        VideoCountInfoDto videoCountInfoDto = VideoCountInfoDto.builder()
                .videoId(adClickCount.getVideoId())
                .views(-1L)
                .likes(-1L)
                .adClicks(adClickCount.getClickCount())
                .build();

        redisTemplate.opsForValue().set(adClickCountKey, "true");
        redisTemplate.expire(adClickCountKey, 60 * 60 * 24, TimeUnit.SECONDS);

        return videoCountInfoDto;
    }
}
