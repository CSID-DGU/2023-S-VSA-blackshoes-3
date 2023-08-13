package com.travelvcommerce.statisticsservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelvcommerce.statisticsservice.dto.AdDto;
import com.travelvcommerce.statisticsservice.dto.TagDto;
import com.travelvcommerce.statisticsservice.dto.VideoInfoDto;
import com.travelvcommerce.statisticsservice.entity.AdClickCount;
import com.travelvcommerce.statisticsservice.entity.TagViewCount;
import com.travelvcommerce.statisticsservice.entity.VideoLikeCount;
import com.travelvcommerce.statisticsservice.entity.VideoViewCount;
import com.travelvcommerce.statisticsservice.repository.*;
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
    @Autowired
    private LikeRepository likeRepository;

    @Override
    @Transactional
    @KafkaListener(topics = "video-create")
    public void createVideo(String payload) {
        log.info("received payload='{}'", payload);

        VideoInfoDto.VideoCreateDto videoCreateDto;

        try {
            videoCreateDto = objectMapper.readValue(payload, VideoInfoDto.VideoCreateDto.class);
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

        VideoInfoDto.VideoUpdateDto videoUpdateDto;

        try {
            videoUpdateDto = objectMapper.readValue(payload, VideoInfoDto.VideoUpdateDto.class);
        } catch (Exception e) {
            log.error("Error converting payload to video", e);
            return;
        }

        String videoId = videoUpdateDto.getVideoId();
        String videoName = videoUpdateDto.getVideoName();
        String sellerId = videoUpdateDto.getSellerId();

        try {
            updateAdClickCount(videoUpdateDto, videoId, videoName, sellerId);
            return;
        } catch (Exception e) {
            log.error("Error updating ad click count", e);
        }

        try {
            updateTagViewCount(videoUpdateDto, videoId, videoName, sellerId);
            return;
        } catch (Exception e) {
            log.error("Error updating tag view count", e);
        }

        try {
            updateVideoName(videoId, videoName);
        } catch (Exception e) {
            log.error("Error updating video name", e);
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

        try {
            likeRepository.deleteAllByVideoId(videoId);
        } catch (Exception e) {
            log.error("Error deleting like", e);
        }
    }

    private void createVideoLikeCount(VideoInfoDto.VideoCreateDto videoCreateDto) {
        VideoLikeCount videoLikeCount = VideoLikeCount.builder()
                .videoId(videoCreateDto.getVideoId())
                .videoName(videoCreateDto.getVideoName())
                .sellerId(videoCreateDto.getSellerId())
                .likeCount(0)
                .build();

        videoLikeCountRepository.save(videoLikeCount);
    }

    private void createVideoViewCount(VideoInfoDto.VideoCreateDto videoCreateDto) {
        VideoViewCount videoViewCount = VideoViewCount.builder()
                .videoId(videoCreateDto.getVideoId())
                .videoName(videoCreateDto.getVideoName())
                .sellerId(videoCreateDto.getSellerId())
                .viewCount(0)
                .build();

        videoViewCountRepository.save(videoViewCount);
    }

    private void createTagViewCount(VideoInfoDto.VideoCreateDto videoCreateDto) {
        List<TagDto> tagDtoList = videoCreateDto.getVideoTags();

        for (TagDto tagDto : tagDtoList) {
            TagViewCount tagViewCount = TagViewCount.builder()
                    .tagId(tagDto.getTagId())
                    .tagName(tagDto.getTagName())
                    .videoId(videoCreateDto.getVideoId())
                    .videoName(videoCreateDto.getVideoName())
                    .sellerId(videoCreateDto.getSellerId())
                    .viewCount(0)
                    .build();

            tagViewCountRepository.save(tagViewCount);
        }
    }

    private void createAdClickCount(VideoInfoDto.VideoCreateDto videoCreateDto) {
        List<AdDto> adDtoList = videoCreateDto.getVideoAds();

        for (AdDto adDto : adDtoList) {
            AdClickCount adClickCount = AdClickCount.builder()
                    .adId(adDto.getAdId())
                    .videoId(videoCreateDto.getVideoId())
                    .videoName(videoCreateDto.getVideoName())
                    .sellerId(videoCreateDto.getSellerId())
                    .clickCount(0)
                    .build();

            adClickCountRepository.save(adClickCount);
        }
    }

    private void updateAdClickCount(VideoInfoDto.VideoUpdateDto videoUpdateDto, String videoId, String videoName, String sellerId) {
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
                            .videoName(videoName)
                            .sellerId(sellerId)
                            .clickCount(0)
                            .build();

                    adClickCountRepository.save(adClickCount);
                }
            });
        }
    }

    private void updateTagViewCount(VideoInfoDto.VideoUpdateDto videoUpdateDto, String videoId, String videoName, String sellerId) {
        if (videoUpdateDto.getVideoTags() != null) {
            List<TagDto> tagDtoList = videoUpdateDto.getVideoTags();

            List<String> newTagIdList = tagDtoList.stream()
                    .map(TagDto::getTagId)
                    .collect(Collectors.toList());

            List<String> oldTagIdList = tagViewCountRepository.findAllByVideoId(videoId).stream()
                    .map(TagViewCount::getTagId)
                    .collect(Collectors.toList());


            oldTagIdList.stream().forEach(tagId -> {
                if (!newTagIdList.contains(tagId)) {
                    tagViewCountRepository.deleteByVideoIdAndTagId(videoId, tagId);
                }
            });

            tagDtoList.stream().forEach(tagDto -> {
                if (!oldTagIdList.contains(tagDto.getTagId())) {
                    TagViewCount tagViewCount = TagViewCount.builder()
                            .tagId(tagDto.getTagId())
                            .tagName(tagDto.getTagName())
                            .videoId(videoId)
                            .videoName(videoName)
                            .sellerId(sellerId)
                            .viewCount(0)
                            .build();

                    tagViewCountRepository.save(tagViewCount);
                }
            });
        }
    }

    private void updateVideoName(String videoId, String videoName) {
        try {
            videoViewCountRepository.findAllByVideoId(videoId).stream()
                    .forEach(videoViewCount -> {
                        videoViewCount.updateVideoName(videoName);
                        videoViewCountRepository.save(videoViewCount);
                    });
        } catch (Exception e) {
            log.error("Error updating video view count", e);
        }

        try {
            tagViewCountRepository.findAllByVideoId(videoId).stream()
                    .forEach(tagViewCount -> {
                        tagViewCount.updateVideoName(videoName);
                        tagViewCountRepository.save(tagViewCount);
                    });
        } catch (Exception e) {
            log.error("Error updating tag view count", e);
        }

        try {
            videoLikeCountRepository.findAllByVideoId(videoId).stream()
                    .forEach(videoLikeCount -> {
                        videoLikeCount.updateVideoName(videoName);
                        videoLikeCountRepository.save(videoLikeCount);
                    });
        } catch (Exception e) {
            log.error("Error updating video like count", e);
        }

        try {
            adClickCountRepository.findAllByVideoId(videoId).stream()
                    .forEach(adClickCount -> {
                        adClickCount.updateVideoName(videoName);
                        adClickCountRepository.save(adClickCount);
                    });
        } catch (Exception e) {
            log.error("Error updating ad click count", e);
        }
    }
}
