package com.travelvcommerce.statisticsservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelvcommerce.statisticsservice.dto.RankDto;
import com.travelvcommerce.statisticsservice.dto.RankResponseDto;
import com.travelvcommerce.statisticsservice.dto.count.TotalAdClickCountDto;
import com.travelvcommerce.statisticsservice.dto.count.TotalTagViewCountDto;
import com.travelvcommerce.statisticsservice.entity.VideoLikeCount;
import com.travelvcommerce.statisticsservice.entity.VideoViewCount;

import com.travelvcommerce.statisticsservice.repository.AdClickCountRepository;
import com.travelvcommerce.statisticsservice.repository.TagViewCountRepository;
import com.travelvcommerce.statisticsservice.repository.VideoLikeCountRepository;
import com.travelvcommerce.statisticsservice.repository.VideoViewCountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
    @Transactional
    public RankResponseDto.VideoViewRankResponseDto getVideoViewRank(String sellerId, int size, boolean refresh) {
        String videoViewRankKey = "videoViewRank:" + sellerId;

        if (!refresh && redisTemplate.hasKey(videoViewRankKey)) {
            String value = redisTemplate.opsForValue().get(videoViewRankKey);
            String aggregatedAt = value.substring(0, value.indexOf("&"));
            String stringVideoViewRankDtoList = value.substring(aggregatedAt.length() + 1);
            List<RankDto.VideoViewRankDto> videoViewRankDtoList = new ArrayList<>();
            try {
                videoViewRankDtoList = objectMapper.readValue(stringVideoViewRankDtoList, objectMapper.getTypeFactory().constructCollectionType(List.class, RankDto.VideoViewRankDto.class));
            } catch (Exception e) {
                log.error("Error parsing video view rank value", e);
            }

            if (videoViewRankDtoList.size() == size) {
                RankResponseDto.VideoViewRankResponseDto videoViewRankResponseDto = RankResponseDto.VideoViewRankResponseDto.builder()
                        .aggregatedAt(aggregatedAt)
                        .videoViewRank(videoViewRankDtoList)
                        .build();

                return videoViewRankResponseDto;
            }
        }

        Pageable pageable = PageRequest.of(0, size);
        List<VideoViewCount> videoViewCountTop10 = videoViewCountRepository.findRankBySellerIdOrderByViewCountDesc(sellerId, pageable);

        List<RankDto.VideoViewRankDto> videoViewRankDtoList = new ArrayList<>();

        videoViewCountTop10.stream().forEach(videoViewCount -> {
            videoViewRankDtoList.add(RankDto.VideoViewRankDto.builder()
                    .videoId(videoViewCount.getVideoId())
                    .videoName(videoViewCount.getVideoName())
                    .views(videoViewCount.getViewCount())
                    .build());
        });

        String aggregatedAt = Timestamp.valueOf(LocalDateTime.now()).toString();

        if (!videoViewRankDtoList.isEmpty()) {
            try {
                String stringVideoViewRankDtoList = objectMapper.writeValueAsString(videoViewRankDtoList);
                String value = aggregatedAt + "&" + stringVideoViewRankDtoList;

                redisTemplate.opsForValue().set(videoViewRankKey, value);
                redisTemplate.expire(videoViewRankKey, 60 * 60 * 1, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("Error caching video view rank value", e);
            }
        }

        RankResponseDto.VideoViewRankResponseDto videoViewRankResponseDto = RankResponseDto.VideoViewRankResponseDto.builder()
                .aggregatedAt(aggregatedAt)
                .videoViewRank(videoViewRankDtoList)
                .build();

        return videoViewRankResponseDto;
    }

    @Override
    @Transactional
    public RankResponseDto.TagViewRankResponseDto getTagViewRank(String sellerId, int size, boolean refresh) {
        String tagViewRankKey = "tagViewRank:" + sellerId;

        if (!refresh && redisTemplate.hasKey(tagViewRankKey)) {
            String value = redisTemplate.opsForValue().get(tagViewRankKey);
            String aggregatedAt = value.substring(0, value.indexOf("&"));
            String stringTagViewRankDtoList = value.substring(aggregatedAt.length() + 1);
            List<RankDto.TagViewRankDto> tagViewRankDtoList = new ArrayList<>();

            try {
                tagViewRankDtoList =  objectMapper.readValue(stringTagViewRankDtoList, objectMapper.getTypeFactory().constructCollectionType(List.class, RankDto.TagViewRankDto.class));
            } catch (Exception e) {
                log.error("Error parsing tag view rank value", e);
            }

            if (tagViewRankDtoList.size() == size) {
                RankResponseDto.TagViewRankResponseDto tagViewRankResponseDto = RankResponseDto.TagViewRankResponseDto.builder()
                        .aggregatedAt(aggregatedAt)
                        .tagViewRank(tagViewRankDtoList)
                        .build();

                return tagViewRankResponseDto;
            }
        }

        Pageable pageable = PageRequest.of(0, size);
        List<TotalTagViewCountDto> tagViewCountTop10 = tagViewCountRepository.findRankBySellerIdOrderByViewCountDesc(sellerId, pageable);

        List<RankDto.TagViewRankDto> tagViewRankDtoList = new ArrayList<>();

        tagViewCountTop10.stream().forEach(tagViewCount -> {
            tagViewRankDtoList.add(RankDto.TagViewRankDto.builder()
                    .tagId(tagViewCount.getTag().getTagId())
                    .tagName(tagViewCount.getTag().getContent())
                    .views(tagViewCount.getTotalViewCount())
                    .build());
        });

        String aggregatedAt = Timestamp.valueOf(LocalDateTime.now()).toString();

        if (!tagViewRankDtoList.isEmpty()) {
            try {
                String stringTagViewRankDtoList = objectMapper.writeValueAsString(tagViewRankDtoList);
                String value = aggregatedAt + "&" + stringTagViewRankDtoList;

                redisTemplate.opsForValue().set(tagViewRankKey, value);
                redisTemplate.expire(tagViewRankKey, 60 * 60 * 1, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("Error caching tag view rank value", e);
            }
        }

        RankResponseDto.TagViewRankResponseDto tagViewRankResponseDto = RankResponseDto.TagViewRankResponseDto.builder()
                .aggregatedAt(aggregatedAt)
                .tagViewRank(tagViewRankDtoList)
                .build();

        return tagViewRankResponseDto;
    }

    @Override
    @Transactional
    public RankResponseDto.VideoLikeRankResponseDto getVideoLikeRank(String sellerId, int size, boolean refresh) {
        String videoLikeRankKey = "videoLikeRank:" + sellerId;

        if (!refresh && redisTemplate.hasKey(videoLikeRankKey)) {
            String value = redisTemplate.opsForValue().get(videoLikeRankKey);
            String aggregatedAt = value.substring(0, value.indexOf("&"));
            String stringVideoLikeRankDtoList = value.substring(aggregatedAt.length() + 1);
            List<RankDto.VideoLikeRankDto> videoLikeRankDtoList = new ArrayList<>();
            try {
                videoLikeRankDtoList =  objectMapper.readValue(stringVideoLikeRankDtoList, objectMapper.getTypeFactory().constructCollectionType(List.class, RankDto.VideoLikeRankDto.class));
            } catch (Exception e) {
                log.error("Error parsing video like rank value", e);
            }

            if (videoLikeRankDtoList.size() == size) {
                RankResponseDto.VideoLikeRankResponseDto videoLikeRankResponseDto = RankResponseDto.VideoLikeRankResponseDto.builder()
                        .aggregatedAt(aggregatedAt)
                        .videoLikeRank(videoLikeRankDtoList)
                        .build();

                return videoLikeRankResponseDto;
            }
        }

        Pageable pageable = PageRequest.of(0, size);
        List<VideoLikeCount> videoLikeCountTop10 = videoLikeCountRepository.findRankBySellerIdOrderByLikeCountDesc(sellerId, pageable);

        List<RankDto.VideoLikeRankDto> videoLikeRankDtoList = new ArrayList<>();

        videoLikeCountTop10.stream().forEach(videoLikeCount -> {
            videoLikeRankDtoList.add(RankDto.VideoLikeRankDto.builder()
                    .videoId(videoLikeCount.getVideoId())
                    .videoName(videoLikeCount.getVideoName())
                    .likes(videoLikeCount.getLikeCount())
                    .build());
        });

        String aggregatedAt = Timestamp.valueOf(LocalDateTime.now()).toString();

        if (!videoLikeRankDtoList.isEmpty()) {
            try {
                String stringVideoLikeRankDtoList = objectMapper.writeValueAsString(videoLikeRankDtoList);
                String value = aggregatedAt + "&" + stringVideoLikeRankDtoList;

                redisTemplate.opsForValue().set(videoLikeRankKey, value);
                redisTemplate.expire(videoLikeRankKey, 60 * 60 * 1, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("Error caching video like rank value", e);
            }
        }

        RankResponseDto.VideoLikeRankResponseDto videoLikeRankResponseDto = RankResponseDto.VideoLikeRankResponseDto.builder()
                .aggregatedAt(aggregatedAt)
                .videoLikeRank(videoLikeRankDtoList)
                .build();

        return videoLikeRankResponseDto;
    }

    @Override
    @Transactional
    public RankResponseDto.VideoAdClickRankResponseDto getAdClickRank(String sellerId, int size, boolean refresh) {
        String adClickRankKey = "adClickRank:" + sellerId;

        if (!refresh && redisTemplate.hasKey(adClickRankKey)) {
            String value = redisTemplate.opsForValue().get(adClickRankKey);
            String aggregatedAt = value.substring(0, value.indexOf("&"));
            String stringVideoAdClickRankDtoList = value.substring(aggregatedAt.length() + 1);

            List<RankDto.VideoAdClickRankDto> videoAdClickRankDtoList = new ArrayList<>();
            try {
                videoAdClickRankDtoList =  objectMapper.readValue(stringVideoAdClickRankDtoList, objectMapper.getTypeFactory().constructCollectionType(List.class, RankDto.VideoAdClickRankDto.class));
            } catch (Exception e) {
                log.error("Error parsing ad click rank value", e);
            }

            if (videoAdClickRankDtoList.size() == size) {
                RankResponseDto.VideoAdClickRankResponseDto videoAdClickRankResponseDto = RankResponseDto.VideoAdClickRankResponseDto.builder()
                        .aggregatedAt(aggregatedAt)
                        .videoAdClickRank(videoAdClickRankDtoList)
                        .build();

                return videoAdClickRankResponseDto;
            }
        }

        Pageable pageable = PageRequest.of(0, size);
        List<TotalAdClickCountDto> adClickCountTop10 = adClickCountRepository.findRankBySellerIdOrderByClickCountDesc(sellerId, pageable);

        List<RankDto.VideoAdClickRankDto> videoAdClickRankDtoList = new ArrayList<>();

        adClickCountTop10.stream().forEach(adClickCount -> {
            videoAdClickRankDtoList.add(RankDto.VideoAdClickRankDto.builder()
                    .videoId(adClickCount.getVideo().getVideoId())
                    .videoName(adClickCount.getVideo().getVideoName())
                    .adClicks(adClickCount.getTotalAdClickCount())
                    .build());
        });

        String aggregatedAt = Timestamp.valueOf(LocalDateTime.now()).toString();

        if (!videoAdClickRankDtoList.isEmpty()) {
            try {
                String stringVideoAdClickRankDtoList = objectMapper.writeValueAsString(videoAdClickRankDtoList);
                String value = aggregatedAt + "&" + stringVideoAdClickRankDtoList;

                redisTemplate.opsForValue().set(adClickRankKey, value);
                redisTemplate.expire(adClickRankKey, 60 * 60 * 1, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("Error caching ad click rank value", e);
            }
        }

        RankResponseDto.VideoAdClickRankResponseDto videoAdClickRankResponseDto = RankResponseDto.VideoAdClickRankResponseDto.builder()
                .aggregatedAt(aggregatedAt)
                .videoAdClickRank(videoAdClickRankDtoList)
                .build();

        return videoAdClickRankResponseDto;
    }
}
