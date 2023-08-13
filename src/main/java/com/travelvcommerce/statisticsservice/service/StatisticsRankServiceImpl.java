package com.travelvcommerce.statisticsservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelvcommerce.statisticsservice.dto.RankDto;
import com.travelvcommerce.statisticsservice.entity.AdClickCount;
import com.travelvcommerce.statisticsservice.entity.TagViewCount;
import com.travelvcommerce.statisticsservice.entity.VideoLikeCount;
import com.travelvcommerce.statisticsservice.entity.VideoViewCount;
import com.travelvcommerce.statisticsservice.repository.AdClickCountRepository;
import com.travelvcommerce.statisticsservice.repository.TagViewCountRepository;
import com.travelvcommerce.statisticsservice.repository.VideoLikeCountRepository;
import com.travelvcommerce.statisticsservice.repository.VideoViewCountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class StatisticsRankServiceImpl implements StatisticsRankService {
    @Autowired
    private VideoViewCountRepository videoViewCountRepository;
    @Autowired
    private TagViewCountRepository tagViewCountRepository;
    @Autowired
    private VideoLikeCountRepository videoLikeCountRepository;
    @Autowired
    private AdClickCountRepository adClickCountRepository;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<RankDto.VideoViewRankDto> getVideoViewTop10(String sellerId) {
        String videoViewRankKey = "videoViewRank:" + sellerId;
        if (redisTemplate.hasKey(videoViewRankKey)) {
            String videoViewRankValue = redisTemplate.opsForValue().get(videoViewRankKey);
            try {
                return objectMapper.readValue(videoViewRankValue, objectMapper.getTypeFactory().constructCollectionType(List.class, RankDto.VideoViewRankDto.class));
            } catch (Exception e) {
                log.error("Error parsing video view rank value", e);
            }
        }

        List<VideoViewCount> videoViewCountTop10 = videoViewCountRepository.findTop10BySellerIdOrderByCountDesc(sellerId);

        List<RankDto.VideoViewRankDto> videoViewRankDtoList = new ArrayList<>();

        videoViewCountTop10.stream().forEach(videoViewCount -> {
            videoViewRankDtoList.add(RankDto.VideoViewRankDto.builder()
                    .videoId(videoViewCount.getVideoId())
                    .videoName(videoViewCount.getVideoName())
                    .views(videoViewCount.getViewCount())
                    .build());
        });

        try {
            redisTemplate.opsForValue().set(videoViewRankKey, objectMapper.writeValueAsString(videoViewRankDtoList));
            redisTemplate.expire(videoViewRankKey, 60 * 60 * 6, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Error caching video view rank value", e);
        }

        return videoViewRankDtoList;
    }

    @Override
    public List<RankDto.TagViewRankDto> getTagViewTop10(String sellerId) {
        String tagViewRankKey = "tagViewRank:" + sellerId;

        if (redisTemplate.hasKey(tagViewRankKey)) {
            String tagViewRankValue = redisTemplate.opsForValue().get(tagViewRankKey);
            try {
                return objectMapper.readValue(tagViewRankValue, objectMapper.getTypeFactory().constructCollectionType(List.class, RankDto.TagViewRankDto.class));
            } catch (Exception e) {
                log.error("Error parsing tag view rank value", e);
            }
        }

        List<TagViewCount> tagViewCountTop10 = tagViewCountRepository.findTop10BySellerIdOrderByCountDesc(sellerId);

        List<RankDto.TagViewRankDto> tagViewRankDtoList = new ArrayList<>();

        tagViewCountTop10.stream().forEach(tagViewCount -> {
            tagViewRankDtoList.add(RankDto.TagViewRankDto.builder()
                    .tagId(tagViewCount.getTagId())
                    .tagName(tagViewCount.getTagName())
                    .views(tagViewCount.getViewCount())
                    .build());
        });

        try {
            redisTemplate.opsForValue().set(tagViewRankKey, objectMapper.writeValueAsString(tagViewRankDtoList));
            redisTemplate.expire(tagViewRankKey, 60 * 60 * 6, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Error caching tag view rank value", e);
        }

        return tagViewRankDtoList;
    }

    @Override
    public List<RankDto.VideoLikeRankDto> getVideoLikeTop10(String sellerId) {
        String videoLikeRankKey = "videoLikeRank:" + sellerId;

        if (redisTemplate.hasKey(videoLikeRankKey)) {
            String videoLikeRankValue = redisTemplate.opsForValue().get(videoLikeRankKey);
            try {
                return objectMapper.readValue(videoLikeRankValue, objectMapper.getTypeFactory().constructCollectionType(List.class, RankDto.VideoLikeRankDto.class));
            } catch (Exception e) {
                log.error("Error parsing video like rank value", e);
            }
        }

        List<VideoLikeCount> videoLikeCountTop10 = videoLikeCountRepository.findTop10BySellerIdOrderByCountDesc(sellerId);

        List<RankDto.VideoLikeRankDto> videoLikeRankDtoList = new ArrayList<>();

        videoLikeCountTop10.stream().forEach(videoLikeCount -> {
            videoLikeRankDtoList.add(RankDto.VideoLikeRankDto.builder()
                    .videoId(videoLikeCount.getVideoId())
                    .videoName(videoLikeCount.getVideoName())
                    .likes(videoLikeCount.getLikeCount())
                    .build());
        });

        try {
            redisTemplate.opsForValue().set(videoLikeRankKey, objectMapper.writeValueAsString(videoLikeRankDtoList));
            redisTemplate.expire(videoLikeRankKey, 60 * 60 * 6, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Error caching video like rank value", e);
        }

        return videoLikeRankDtoList;
    }

    @Override
    public List<RankDto.VideoAdClickRankDto> getAdClickTop10(String sellerId) {
        String adClickRankKey = "adClickRank:" + sellerId;

        if (redisTemplate.hasKey(adClickRankKey)) {
            String adClickRankValue = redisTemplate.opsForValue().get(adClickRankKey);
            try {
                return objectMapper.readValue(adClickRankValue, objectMapper.getTypeFactory().constructCollectionType(List.class, RankDto.VideoAdClickRankDto.class));
            } catch (Exception e) {
                log.error("Error parsing ad click rank value", e);
            }
        }

        List<AdClickCount> adClickCountTop10 = adClickCountRepository.findTop10BySellerIdOrderByCountDesc(sellerId);

        List<RankDto.VideoAdClickRankDto> videoAdClickRankDtoList = new ArrayList<>();

        adClickCountTop10.stream().forEach(adClickCount -> {
            videoAdClickRankDtoList.add(RankDto.VideoAdClickRankDto.builder()
                    .adId(adClickCount.getAdId())
                    .videoId(adClickCount.getVideoId())
                    .videoName(adClickCount.getVideoName())
                    .adClicks(adClickCount.getClickCount())
                    .build());
        });

        try {
            redisTemplate.opsForValue().set(adClickRankKey, objectMapper.writeValueAsString(videoAdClickRankDtoList));
            redisTemplate.expire(adClickRankKey, 60 * 60 * 6, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Error caching ad click rank value", e);
        }

        return videoAdClickRankDtoList;
    }
}
