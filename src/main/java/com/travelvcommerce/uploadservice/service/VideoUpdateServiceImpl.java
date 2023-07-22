package com.travelvcommerce.uploadservice.service;

import com.travelvcommerce.uploadservice.dto.AdDto;
import com.travelvcommerce.uploadservice.dto.UploaderDto;
import com.travelvcommerce.uploadservice.dto.VideoDto;
import com.travelvcommerce.uploadservice.entity.*;
import com.travelvcommerce.uploadservice.repository.*;
import com.travelvcommerce.uploadservice.vo.S3Thumbnail;
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
import java.util.stream.Collectors;

@Slf4j
@Service
public class VideoUpdateServiceImpl implements VideoUpdateService {
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private VideoTagRepository videoTagRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private AdRepository adRepository;
    @Autowired
    private UploaderRepository uploaderRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Video getVideo(String userId, String videoId) {
        Video video;

        try {
            video = videoRepository.findByVideoId(videoId).orElseThrow(() -> new Exception("Video not found"));
        } catch (Exception e) {
            log.error("Video not found", e);
            throw new RuntimeException("video not found");
        }

        if (!video.getUploader().getSellerId().equals(userId)) {
            log.error("Invalid user");
            throw new RuntimeException("Invalid user");
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
    public VideoDto.VideoUpdateResponseDto updateThumbnail(Video video, VideoUrl videoUrl, S3Thumbnail s3Thumbnail) {
        videoUrl.setThumbnailS3Url(s3Thumbnail.getS3Url());
        videoUrl.setThumbnailCloudfrontUrl(s3Thumbnail.getCloudfrontUrl());
        VideoDto.VideoUpdateResponseDto videoUpdateResponseDto = updateVideo(video, "Thumbnail");
        return videoUpdateResponseDto;
    }

    @Override
    @Transactional
    public VideoDto.VideoUpdateResponseDto updateTags(String userId, String videoId, List<String> tagIdList) {
        Video video;

        try {
            video = videoRepository.findByVideoId(videoId).orElseThrow(() -> new Exception("Video not found"));
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

        VideoDto.VideoUpdateResponseDto videoUpdateResponseDto = updateVideo(video, "Tags");

        return videoUpdateResponseDto;
    }

    @Override
    @Transactional
    public VideoDto.VideoUpdateResponseDto updateAds(String userId, String videoId, List<AdDto.AdModifyRequestDto> adModifyRequestDtoList) {
        Video video;

        try {
            video = videoRepository.findByVideoId(videoId).orElseThrow(() -> new Exception("Video not found"));
        } catch (Exception e) {
            log.error("Video not found", e);
            throw new RuntimeException("video not found");
        }
        adModifyRequestDtoList.forEach(adModifyRequestDto -> {
            if (adModifyRequestDto.getModifyType().equals("create")) {
                try {
                    Ad ad = modelMapper.map(adModifyRequestDto, Ad.class);
                    ad.setVideo(video);
                    ad.setAdId(UUID.randomUUID().toString());
                    adRepository.save(ad);
                } catch (Exception e) {
                    log.error("create ad error", e);
                    throw new RuntimeException("create ad error");
                }
            }
            else if (adModifyRequestDto.getModifyType().equals("update")) {
                try {
                    Ad ad = adRepository.findByAdId(adModifyRequestDto.getAdId()).orElseThrow(() -> new RuntimeException("ad not found"));
                    ad.setAdUrl(adModifyRequestDto.getAdUrl());
                    ad.setAdContent(adModifyRequestDto.getAdContent());
                    ad.setStartTime(adModifyRequestDto.getStartTime());
                    ad.setEndTime(adModifyRequestDto.getEndTime());
                    adRepository.save(ad);
                } catch (Exception e) {
                    log.error("update ad error", e);
                    throw new RuntimeException("update ad error");
                }
            }
            else if (adModifyRequestDto.getModifyType().equals("delete")) {
                try {
                    Ad ad = adRepository.findByAdId(adModifyRequestDto.getAdId()).orElseThrow(() -> new RuntimeException("ad not found"));
                    adRepository.delete(ad);
                } catch (Exception e) {
                    log.error("delete ad error", e);
                    throw new RuntimeException("delete ad error");
                }
            }
            else {
                log.error("invalid modify type");
                throw new RuntimeException("invalid modify type");
            }
        });

        VideoDto.VideoUpdateResponseDto videoUpdateResponseDto = updateVideo(video, "Ads");

        return videoUpdateResponseDto;
    }

    @Override
    @Transactional
    public List<String> updateUploader(String userId, UploaderDto.UploaderModifyRequestDto uploaderModifyRequestDto) {
        try {
            Uploader uploader = uploaderRepository.findBySellerId(userId)
                    .orElseThrow(() -> new RuntimeException("uploader not found"));

            uploader.setSellerName(uploaderModifyRequestDto.getSellerName());
            uploader.setSellerLogo(uploaderModifyRequestDto.getSellerLogo());

            List<String> videoIdList = uploader.getVideos().stream().map(video -> video.getVideoId()).collect(Collectors.toList());
            return videoIdList;
        } catch (Exception e) {
            log.error("update uploader error", e);
            throw new RuntimeException("update uploader error");
        }
    }

    private VideoDto.VideoUpdateResponseDto updateVideo(Video video, String type) {
        try {
            video.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            videoRepository.save(video);
            return modelMapper.map(video, VideoDto.VideoUpdateResponseDto.class);
        } catch (Exception e) {
            log.error("update video error :" + type, e);
            throw new RuntimeException("update video error :" + type);
        }
    }
}

