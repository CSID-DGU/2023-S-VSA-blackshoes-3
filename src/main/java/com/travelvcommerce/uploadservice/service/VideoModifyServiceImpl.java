package com.travelvcommerce.uploadservice.service;

import com.travelvcommerce.uploadservice.dto.AdDto;
import com.travelvcommerce.uploadservice.entity.Ad;
import com.travelvcommerce.uploadservice.entity.Video;
import com.travelvcommerce.uploadservice.entity.VideoTag;
import com.travelvcommerce.uploadservice.repository.AdRepository;
import com.travelvcommerce.uploadservice.repository.TagRepository;
import com.travelvcommerce.uploadservice.repository.VideoRepository;
import com.travelvcommerce.uploadservice.repository.VideoTagRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class VideoModifyServiceImpl implements VideoModifyService {
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private VideoTagRepository videoTagRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private AdRepository adRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Video getVideo(String userId, String videoId) {
        Video video;

        try {
            video = videoRepository.findBySellerIdAndVideoId(userId, videoId).orElseThrow(() -> new Exception("Video not found"));
        } catch (Exception e) {
            log.error("Video not found", e);
            throw new RuntimeException("video not found");
        }

        return video;
    }

    @Override
    public String getThumbnailS3Key(Video video) {
        try {
        String s3Key = video.getVideoUrl().getThumbnailS3Url().substring(video.getVideoUrl().getThumbnailS3Url().indexOf("videos"));

        return s3Key;
        } catch (Exception e) {
            log.error("get thumbnail s3 key error", e);
            throw new RuntimeException("get thumbnail s3 key error");
        }
    }

    @Override
    public void updateThumbnail(Video video) {
        updateVideo(video, "Thumbnail");
    }

    @Override
    @Transactional
    public void updateTags(String userId, String videoId, List<String> tagIdList) {
        Video video;

        try {
            video = videoRepository.findBySellerIdAndVideoId(userId, videoId).orElseThrow(() -> new Exception("Video not found"));
        } catch (Exception e) {
            log.error("Video not found", e);
            throw new RuntimeException("video not found");
        }

        List<VideoTag> videoTagList = video.getVideoTags();

        try {
            List<VideoTag> videoTagsToDelete = new ArrayList<>();
            videoTagList.stream().filter(videoTag -> !tagIdList.contains(videoTag.getTag().getTagId())).forEach(videoTag -> {
                videoTagsToDelete.add(videoTag);
            });
            for (VideoTag videoTag : videoTagsToDelete) {
                videoTagList.remove(videoTag);
                videoTagRepository.delete(videoTag);
            }
        } catch (Exception e) {
            log.error("delete video tag error", e);
            throw new RuntimeException("delete video tag error");
        }

        try {
            tagIdList.stream().filter(tagId -> videoTagList.stream().noneMatch(videoTag -> videoTag.getTag().getTagId().equals(tagId))).forEach(tagId -> {
                VideoTag videoTag = new VideoTag();
                videoTag.setVideo(video);
                videoTag.setTag(tagRepository.findByTagId(tagId).orElseThrow(() -> new RuntimeException("tag not found")));
                videoTagRepository.save(videoTag);
            });
        } catch (Exception e) {
            log.error("add video tag error", e);
            throw new RuntimeException("add video tag error");
        }

        updateVideo(video, "Tags");
    }

    @Override
    public void updateAds(String userId, String videoId, List<AdDto.AdRequestDto> adRequestDtoList) {
        Video video;

        try {
            video = videoRepository.findBySellerIdAndVideoId(userId, videoId).orElseThrow(() -> new Exception("Video not found"));
        } catch (Exception e) {
            log.error("Video not found", e);
            throw new RuntimeException("video not found");
        }

        adRequestDtoList.forEach(adRequestDto -> {
            if (adRequestDto.getAdId().equals("new")) {
                try {
                    Ad ad = modelMapper.map(adRequestDto, Ad.class);
                    ad.setVideo(video);
                    ad.setAdId(UUID.randomUUID().toString());
                    adRepository.save(ad);
                } catch (Exception e) {
                    log.error("add ad error", e);
                    throw new RuntimeException("add ad error");
                }
            } else {
                try {
                Ad ad = adRepository.findByAdId(adRequestDto.getAdId()).orElseThrow(() -> new RuntimeException("ad not found"));
                ad.setAdUrl(adRequestDto.getAdUrl());
                ad.setAdContent(adRequestDto.getAdContent());
                ad.setStartTime(adRequestDto.getStartTime());
                ad.setEndTime(adRequestDto.getEndTime());
                adRepository.save(ad);
                } catch (Exception e) {
                    log.error("update ad error", e);
                    throw new RuntimeException("update ad error");
                }
            }
        });

        updateVideo(video, "Ads");
    }

    private void updateVideo(Video video, String type) {
        try {
            video.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            videoRepository.save(video);
        } catch (Exception e) {
            log.error("update video error :" + type, e);
            throw new RuntimeException("update video error :" + type);
        }
    }
}

