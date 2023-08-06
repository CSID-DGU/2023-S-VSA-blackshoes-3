package com.travelvcommerce.statisticsservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelvcommerce.statisticsservice.dto.AdDto;
import com.travelvcommerce.statisticsservice.dto.TagDto;
import com.travelvcommerce.statisticsservice.dto.VideoDto;
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
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class KafkaVideoInfoConsumerImpl implements KafkaVideoInfoConsumer {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AdClickCountRepository adClickCountRepository;
    @Autowired
    private TagViewCountRepository tagViewCountRepository;
    @Autowired
    private VideoViewCountRepository videoViewCountRepository;
    @Autowired
    private VideoLikeCountRepository videoLikeCountRepository;


    @Override
    @KafkaListener(topics = "video-create")
    public void createVideo(String payload) {
        log.info("received payload='{}'", payload);

        VideoDto.VideoCreateDto videoCreateDto;

        try {
            videoCreateDto = objectMapper.readValue(payload, VideoDto.VideoCreateDto.class);
        } catch (Exception e) {
            log.error("Error converting payload to video", e);
            return;
        }

        try {
            VideoLikeCount videoLikeCount = VideoLikeCount.builder()
                    .videoId(videoCreateDto.getVideoId())
                    .sellerId(videoCreateDto.getSellerId())
                    .likeCount(0)
                    .build();

            videoLikeCountRepository.save(videoLikeCount);
        } catch (Exception e) {
            log.error("Error saving video like count", e);
        }

        try {
            VideoViewCount videoViewCount = VideoViewCount.builder()
                    .videoId(videoCreateDto.getVideoId())
                    .sellerId(videoCreateDto.getSellerId())
                    .viewCount(0)
                    .build();

            videoViewCountRepository.save(videoViewCount);
        } catch (Exception e) {
            log.error("Error saving video view count", e);
        }

        try {
            List<AdDto> adDtoList = videoCreateDto.getVideoAds();

            for (AdDto adDto : adDtoList) {
                AdClickCount adClickCount = AdClickCount.builder()
                        .adId(adDto.getAdId())
                        .videoId(videoCreateDto.getVideoId())
                        .sellerId(videoCreateDto.getSellerId())
                        .clickCount(0)
                        .build();

                adClickCountRepository.save(adClickCount);
            }
        } catch (Exception e) {
            log.error("Error saving ad click count", e);
        }

        try {
            List<TagDto> tagDtoList = videoCreateDto.getVideoTags();

            for (TagDto tagDto : tagDtoList) {
                TagViewCount tagViewCount = TagViewCount.builder()
                        .tagId(tagDto.getTagId())
                        .sellerId(videoCreateDto.getSellerId())
                        .viewCount(0)
                        .build();

                tagViewCountRepository.save(tagViewCount);
            }
        } catch (Exception e) {
            log.error("Error saving tag view count", e);
        }

    }

    @Override
    public void updateVideo(String payload) {
        log.info("received payload='{}'", payload);
    }

    @Override
    public void deleteVideo(String payload) {
        log.info("received payload='{}'", payload);
    }
}
