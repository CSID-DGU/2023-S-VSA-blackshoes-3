package com.travelvcommerce.statisticsservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelvcommerce.statisticsservice.dto.RankDto;
import com.travelvcommerce.statisticsservice.dto.RankResponseDto;
import com.travelvcommerce.statisticsservice.dto.TagRankDto;
import com.travelvcommerce.statisticsservice.repository.TagViewCountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class TagRankServiceImpl implements TagRankService {
    private final TagViewCountRepository tagViewCountRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public RankResponseDto.TagRankResponseDto getTagRankByRegion() {
        String tagRegionRankKey = "tagRegionRank";

        if (redisTemplate.hasKey(tagRegionRankKey)) {
            String value = redisTemplate.opsForValue().get(tagRegionRankKey);
            String aggregatedAt = value.substring(0, value.indexOf("&"));
            String stringTagRegionRankDtoList = value.substring(value.indexOf("&") + 1);
            List<TagRankDto> tagRegionRankDtoList = new ArrayList<>();
            try {
                tagRegionRankDtoList =  objectMapper.readValue(stringTagRegionRankDtoList, objectMapper.getTypeFactory().constructCollectionType(List.class, TagRankDto.class));
            } catch (Exception e) {
                log.error("Error parsing tag region rank value", e);
            }

            RankResponseDto.TagRankResponseDto tagRankResponseDto = RankResponseDto.TagRankResponseDto.builder()
                    .tagRank(tagRegionRankDtoList)
                    .aggregatedAt(aggregatedAt)
                    .build();

            return tagRankResponseDto;
        }

        Pageable pageable = Pageable.ofSize(10);
        List<TagRankDto> tagRegionRankDtoList = tagViewCountRepository.findRegionRank(pageable);
        String aggregatedAt = Timestamp.valueOf(LocalDateTime.now()).toString();

        try {
            String stringVideoLikeRankDtoList = objectMapper.writeValueAsString(tagRegionRankDtoList);
            String value = aggregatedAt + "&" + stringVideoLikeRankDtoList;

            redisTemplate.opsForValue().set(tagRegionRankKey, value);
            redisTemplate.expire(tagRegionRankKey, 60 * 60 * 1, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Error caching video like rank value", e);
        }

        RankResponseDto.TagRankResponseDto tagRankResponseDto = RankResponseDto.TagRankResponseDto.builder()
                .tagRank(tagRegionRankDtoList)
                .aggregatedAt(aggregatedAt)
                .build();

        return tagRankResponseDto;
    }

    @Override
    public RankResponseDto.TagRankResponseDto getTagRankByTheme() {
        String tagThemeRankKey = "tagThemeRank";

        if(redisTemplate.hasKey(tagThemeRankKey)) {
            String value = redisTemplate.opsForValue().get(tagThemeRankKey);
            String aggregatedAt = value.substring(0, value.indexOf("&"));
            String stringTagThemeRankDtoList = value.substring(value.indexOf("&") + 1);
            List<TagRankDto> tagThemeRankDtoList = new ArrayList<>();
            try {
                tagThemeRankDtoList =  objectMapper.readValue(stringTagThemeRankDtoList, objectMapper.getTypeFactory().constructCollectionType(List.class, TagRankDto.class));
            } catch (Exception e) {
                log.error("Error parsing tag theme rank value", e);
            }

            RankResponseDto.TagRankResponseDto tagRankResponseDto = RankResponseDto.TagRankResponseDto.builder()
                    .tagRank(tagThemeRankDtoList)
                    .aggregatedAt(aggregatedAt)
                    .build();

            return tagRankResponseDto;
        }

        Pageable pageable = Pageable.ofSize(10);
        List<TagRankDto> tagThemeRankDtoList = tagViewCountRepository.findThemeRank(pageable);
        String aggregatedAt = Timestamp.valueOf(LocalDateTime.now()).toString();

        try {
            String stringVideoLikeRankDtoList = objectMapper.writeValueAsString(tagThemeRankDtoList);
            String value = aggregatedAt + "&" + stringVideoLikeRankDtoList;

            redisTemplate.opsForValue().set(tagThemeRankKey, value);
            redisTemplate.expire(tagThemeRankKey, 60 * 60 * 1, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Error caching video like rank value", e);
        }

        RankResponseDto.TagRankResponseDto tagRankResponseDto = RankResponseDto.TagRankResponseDto.builder()
                .tagRank(tagThemeRankDtoList)
                .aggregatedAt(aggregatedAt)
                .build();

        return tagRankResponseDto;
    }
}
