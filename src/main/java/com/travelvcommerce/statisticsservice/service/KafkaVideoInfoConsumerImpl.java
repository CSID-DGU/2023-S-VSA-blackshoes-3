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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    @Transactional
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
            createVideoLikeCount(videoCreateDto);
        } catch (Exception e) {
            log.error("Error creating video like count", e);
        }

        try {
            createVideoViewCount(videoCreateDto);
        } catch (Exception e) {
            log.error("Error creating video view count", e);
        }

        try {
            createTagViewCount(videoCreateDto);
        } catch (Exception e) {
            log.error("Error creating tag view count", e);
        }

        try {
            createAdClickCount(videoCreateDto);
        } catch (Exception e) {
            log.error("Error creating ad click count", e);
        }
    }

    @Override
    @Transactional
    @KafkaListener(topics = "video-update")
    public void updateVideo(String payload) {
        log.info("received payload='{}'", payload);

        VideoDto.VideoUpdateDto videoUpdateDto;

        try {
            videoUpdateDto = objectMapper.readValue(payload, VideoDto.VideoUpdateDto.class);
        } catch (Exception e) {
            log.error("Error converting payload to video", e);
            return;
        }

        String videoId = videoUpdateDto.getVideoId();
        String sellerId = videoUpdateDto.getSellerId();

        try {
            updateAdClickCount(videoUpdateDto, videoId, sellerId);
        } catch (Exception e) {
            log.error("Error updating ad click count", e);
        }

        try {
            updateTagViewCount(videoUpdateDto, videoId, sellerId);
        } catch (Exception e) {
            log.error("Error updating tag view count", e);
        }
    }

    @Override
    @Transactional
    @KafkaListener(topics = "video-delete")
    public void deleteVideo(String payload) {
        log.info("received payload='{}'", payload);
        String videoId = payload;

        try {
            videoViewCountRepository.deleteAllByVideoId(videoId);
        } catch (Exception e) {
            log.error("Error deleting video view count", e);
        }

        try {
            tagViewCountRepository.deleteAllByVideoId(videoId);
        } catch (Exception e) {
            log.error("Error deleting tag view count", e);
        }

        try {
            videoLikeCountRepository.deleteAllByVideoId(videoId);
        } catch (Exception e) {
            log.error("Error deleting video like count", e);
        }

        try {
            adClickCountRepository.deleteAllByVideoId(videoId);
        } catch (Exception e) {
            log.error("Error deleting ad click count", e);
        }
    }

    private void createVideoLikeCount(VideoDto.VideoCreateDto videoCreateDto) {
        VideoLikeCount videoLikeCount = VideoLikeCount.builder()
                .videoId(videoCreateDto.getVideoId())
                .sellerId(videoCreateDto.getSellerId())
                .likeCount(0)
                .build();

        videoLikeCountRepository.save(videoLikeCount);
    }

    private void createVideoViewCount(VideoDto.VideoCreateDto videoCreateDto) {
        VideoViewCount videoViewCount = VideoViewCount.builder()
                .videoId(videoCreateDto.getVideoId())
                .sellerId(videoCreateDto.getSellerId())
                .viewCount(0)
                .build();

        videoViewCountRepository.save(videoViewCount);
    }

    private void createTagViewCount(VideoDto.VideoCreateDto videoCreateDto) {
        List<TagDto> tagDtoList = videoCreateDto.getVideoTags();

        for (TagDto tagDto : tagDtoList) {
            TagViewCount tagViewCount = TagViewCount.builder()
                    .tagId(tagDto.getTagId())
                    .videoId(videoCreateDto.getVideoId())
                    .sellerId(videoCreateDto.getSellerId())
                    .viewCount(0)
                    .build();

            tagViewCountRepository.save(tagViewCount);
        }
    }

    private void createAdClickCount(VideoDto.VideoCreateDto videoCreateDto) {
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
    }

    private void updateAdClickCount(VideoDto.VideoUpdateDto videoUpdateDto, String videoId, String sellerId) {
        if (videoUpdateDto.getVideoAds() != null) {
            List<String> newAdIdList = videoUpdateDto.getVideoAds().stream()
                    .map(AdDto::getAdId)
                    .collect(Collectors.toList());

            List<String> oldAdIdList = adClickCountRepository.findAllByVideoId(videoId).stream()
                    .map(AdClickCount::getAdId)
                    .collect(Collectors.toList());

            oldAdIdList.stream().forEach(adId -> {
                if (!newAdIdList.contains(adId)) {
                    adClickCountRepository.deleteByVideoIdAndAdId(videoId, adId);
                }
            });

            newAdIdList.stream().forEach(adId -> {
                if (!oldAdIdList.contains(adId)) {
                    AdClickCount adClickCount = AdClickCount.builder()
                            .adId(adId)
                            .videoId(videoId)
                            .sellerId(sellerId)
                            .clickCount(0)
                            .build();

                    adClickCountRepository.save(adClickCount);
                }
            });
        }
    }

    private void updateTagViewCount(VideoDto.VideoUpdateDto videoUpdateDto, String videoId, String sellerId) {
        if (videoUpdateDto.getVideoTags() != null) {
            List<String> newTagIdList = videoUpdateDto.getVideoTags().stream()
                    .map(TagDto::getTagId)
                    .collect(Collectors.toList());

            List<String> oldTagIdList = tagViewCountRepository.findAllByVideoIdAndSellerId(videoId, sellerId).stream()
                    .map(TagViewCount::getTagId)
                    .collect(Collectors.toList());


            oldTagIdList.stream().forEach(tagId -> {
                if (!newTagIdList.contains(tagId)) {
                    tagViewCountRepository.deleteByVideoIdAndTagId(videoId, tagId);
                }
            });

            newTagIdList.stream().forEach(tagId -> {
                if (!oldTagIdList.contains(tagId)) {
                    TagViewCount tagViewCount = TagViewCount.builder()
                            .tagId(tagId)
                            .videoId(videoId)
                            .sellerId(sellerId)
                            .viewCount(0)
                            .build();

                    tagViewCountRepository.save(tagViewCount);
                }
            });
        }
    }
}
