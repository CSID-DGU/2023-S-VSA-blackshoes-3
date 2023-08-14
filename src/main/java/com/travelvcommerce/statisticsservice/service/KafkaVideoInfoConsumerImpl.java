package com.travelvcommerce.statisticsservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelvcommerce.statisticsservice.dto.AdDto;
import com.travelvcommerce.statisticsservice.dto.TagDto;
import com.travelvcommerce.statisticsservice.dto.VideoInfoDto;
import com.travelvcommerce.statisticsservice.entity.*;
import com.travelvcommerce.statisticsservice.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private VideoRepository videoRepository;

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

        Video video;
        try {
            video = createVideoEntity(videoCreateDto);
        } catch (Exception e) {
            log.error("Error creating video", e);
            return;
        }

        try {
            createVideoLikeCount(video);
        } catch (Exception e) {
            log.error("Error creating video like count", e);
        }

        try {
            createVideoViewCount(video);
        } catch (Exception e) {
            log.error("Error creating video view count", e);
        }

        try {
            List<String> tagIds = videoCreateDto.getVideoTags().stream().map(TagDto::getTagId).collect(Collectors.toList());
            createTagViewCount(video, tagIds);
        } catch (Exception e) {
            log.error("Error creating tag view count", e);
        }

        try {
            List<String> adIds = videoCreateDto.getVideoAds().stream().map(AdDto::getAdId).collect(Collectors.toList());
            createAdClickCount(video, adIds);
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

        Video video = videoRepository.findByVideoId(videoId).orElseThrow(() -> new RuntimeException("Video not found"));

        try {
            updateAdClickCount(video, videoUpdateDto);
            return;
        } catch (Exception e) {
            log.error("Error updating ad click count", e);
        }

        try {
            updateTagViewCount(video, videoUpdateDto);
            return;
        } catch (Exception e) {
            log.error("Error updating tag view count", e);
        }

        try {
            updateVideoName(video, videoUpdateDto);
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
            videoRepository.deleteByVideoId(videoId);
        } catch (Exception e) {
            log.error("Error deleting video", e);
        }
    }

    private Video createVideoEntity(VideoInfoDto.VideoCreateDto videoCreateDto) {
        Video video = Video.builder()
                .videoId(videoCreateDto.getVideoId())
                .videoName(videoCreateDto.getVideoName())
                .sellerId(videoCreateDto.getSellerId())
                .build();

        videoRepository.save(video);

        return video;
    }

    private void createVideoLikeCount(Video video) {
        VideoLikeCount videoLikeCount = VideoLikeCount.builder()
                .video(video)
                .likeCount(0)
                .build();

        videoLikeCountRepository.save(videoLikeCount);
    }

    private void createVideoViewCount(Video video) {
        VideoViewCount videoViewCount = VideoViewCount.builder()
                .video(video)
                .viewCount(0)
                .build();

        videoViewCountRepository.save(videoViewCount);
    }

    private void createTagViewCount(Video video, List<String> tagIds) {

        for (String tagId : tagIds) {
            TagViewCount tagViewCount = TagViewCount.builder()
                    .tag(tagRepository.findByTagId(tagId).get())
                    .video(video)
                    .viewCount(0)
                    .build();

            tagViewCountRepository.save(tagViewCount);
        }
    }

    private void createAdClickCount(Video video, List<String> adIds) {
        for (String adId : adIds) {
            AdClickCount adClickCount = AdClickCount.builder()
                    .adId(adId)
                    .video(video)
                    .clickCount(0)
                    .build();

            adClickCountRepository.save(adClickCount);
        }
    }

    private void updateAdClickCount(Video video, VideoInfoDto.VideoUpdateDto videoUpdateDto) {
        if (videoUpdateDto.getVideoAds() == null) {
            return;
        }

        List<String> newAdIdList = videoUpdateDto.getVideoAds().stream()
                .map(AdDto::getAdId)
                .collect(Collectors.toList());

        List<String> oldAdIdList = video.getAdClickCounts().stream()
                .map(AdClickCount::getAdId)
                .collect(Collectors.toList());

        oldAdIdList.stream().forEach(adId -> {
            if (!newAdIdList.contains(adId)) {
                adClickCountRepository.deleteByAdId(adId);
            }
        });

        newAdIdList.stream().forEach(adId -> {
            if (!oldAdIdList.contains(adId)) {
                AdClickCount adClickCount = AdClickCount.builder()
                        .adId(adId)
                        .video(video)
                        .clickCount(0)
                        .build();

                adClickCountRepository.save(adClickCount);
            }
        });
    }

    private void updateTagViewCount(Video video, VideoInfoDto.VideoUpdateDto videoUpdateDto) {
        if (videoUpdateDto.getVideoTags() == null) {
            return;
        }

        List<TagDto> tagDtoList = videoUpdateDto.getVideoTags();

        List<String> newTagIdList = tagDtoList.stream()
                .map(TagDto::getTagId)
                .collect(Collectors.toList());

        List<String> oldTagIdList = video.getTagViewCounts().stream()
                .map(TagViewCount::getTagId)
                .collect(Collectors.toList());


        oldTagIdList.stream().forEach(tagId -> {
            if (!newTagIdList.contains(tagId)) {
                tagViewCountRepository.deleteByVideoIdAndTagId(video.getVideoId(), tagId);
            }
        });

        tagDtoList.stream().forEach(tagDto -> {
            if (!oldTagIdList.contains(tagDto.getTagId())) {
                TagViewCount tagViewCount = TagViewCount.builder()
                        .tag(tagRepository.findByTagId(tagDto.getTagId()).get())
                        .video(video)
                        .viewCount(0)
                        .build();

                tagViewCountRepository.save(tagViewCount);
            }
        });
    }

    private void updateVideoName(Video video, VideoInfoDto.VideoUpdateDto videoUpdateDto) {
        if (videoUpdateDto.getVideoName() == null) {
            return;
        }

        String videoName = videoUpdateDto.getVideoName();

        try {
            video.updateVideoName(videoName);
            ;
        } catch (Exception e) {
            log.error("Error updating video name", e);
        }
    }
}
