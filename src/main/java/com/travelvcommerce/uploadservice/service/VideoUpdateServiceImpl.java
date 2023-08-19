package com.travelvcommerce.uploadservice.service;

import com.travelvcommerce.uploadservice.dto.*;
import com.travelvcommerce.uploadservice.entity.*;
import com.travelvcommerce.uploadservice.repository.*;
import com.travelvcommerce.uploadservice.vo.S3Thumbnail;
import com.travelvcommerce.uploadservice.vo.UpdatedField;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
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
        Video video = videoRepository.findByVideoId(videoId).orElseThrow(() -> new NoSuchElementException("Video not found"));

        if (!video.getUploader().getSellerId().equals(userId)) {
            log.error("Invalid user");
            throw new IllegalArgumentException("Invalid user");
        }

        return video;
    }

    @Override
    @Transactional
    public DenormalizedVideoDto updateThumbnail(String userId, String videoId,
                                                MultipartFile thumbnail, AwsS3Service awsS3Service) {
        Video video = getVideo(userId, videoId);
        VideoUrl videoUrl = video.getVideoUrl();

        S3Thumbnail s3Thumbnail = awsS3Service.updateThumbnail(videoUrl.getThumbnailS3Url(), thumbnail);

        videoUrl.updateThumbnail(s3Thumbnail.getS3Url(), s3Thumbnail.getCloudfrontUrl());

        video.updateUpdatedAt();

        DenormalizedVideoDto denormalizedVideoDto = denormalizeVideo(video, UpdatedField.THUMBNAIL);

        return denormalizedVideoDto;
    }

    @Override
    @Transactional
    public DenormalizedVideoDto updateTags(String userId, String videoId, List<String> tagIdList) {
        Video video = getVideo(userId, videoId);

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

                Tag tag = tagRepository.findByTagId(tagId).orElseThrow(() -> new RuntimeException("tag not found"));

                VideoTag videoTag = VideoTag.builder()
                        .video(video)
                        .tag(tag)
                        .build();
                video.getVideoTags().add(videoTag);
            });
        } catch (Exception e) {
            log.error("add video tag error", e);
            throw new RuntimeException("add video tag error");
        }

        video.setVideoTags(videoTagList);

        video.updateUpdatedAt();

        DenormalizedVideoDto denormalizedVideoDto = denormalizeVideo(video, UpdatedField.TAGS);

        return denormalizedVideoDto;
    }

    @Override
    @Transactional
    public DenormalizedVideoDto updateAds(String userId, String videoId, List<AdDto.AdModifyRequestDto> adModifyRequestDtoList) {
        Video video = getVideo(userId, videoId);

        List<Ad> adList = video.getAds();

        adModifyRequestDtoList.forEach(adModifyRequestDto -> {
            if (adModifyRequestDto.getModifyType().equals("create")) {
                try {
                    Ad ad = Ad.builder()
                            .adId(UUID.randomUUID().toString())
                            .adUrl(adModifyRequestDto.getAdUrl())
                            .adContent(adModifyRequestDto.getAdContent())
                            .startTime(adModifyRequestDto.getStartTime())
                            .endTime(adModifyRequestDto.getEndTime())
                            .build();

                    ad.setVideo(video);

                    adRepository.save(ad);

                    adList.add(ad);
                } catch (Exception e) {
                    log.error("create ad error", e);
                    throw new RuntimeException("create ad error");
                }
            }
            else if (adModifyRequestDto.getModifyType().equals("update")) {
                Ad ad = adRepository.findByAdId(adModifyRequestDto.getAdId()).orElseThrow(() -> new NoSuchElementException("ad not found"));
                adList.remove(ad);
                try {
                    ad.update(adModifyRequestDto.getAdUrl(),
                            adModifyRequestDto.getAdContent(),
                            adModifyRequestDto.getStartTime(),
                            adModifyRequestDto.getEndTime());

                    adList.add(ad);
                } catch (Exception e) {
                    log.error("update ad error", e);
                    throw new RuntimeException("update ad error");
                }
            }
            else if (adModifyRequestDto.getModifyType().equals("delete")) {
                Ad ad = adRepository.findByAdId(adModifyRequestDto.getAdId()).orElseThrow(() -> new NoSuchElementException("ad not found"));
                try {
                    adRepository.delete(ad);
                    adList.remove(ad);
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

        video.setAds(adList);

        video.updateUpdatedAt();

        DenormalizedVideoDto denormalizedVideoDto = denormalizeVideo(video, UpdatedField.ADS);

        return denormalizedVideoDto;
    }

    @Transactional
    @Override
    public DenormalizedVideoDto updateVideoName(String userId, String videoId, String videoName) {
        Video video = getVideo(userId, videoId);

        video.updateVideoName(videoName);

        DenormalizedVideoDto denormalizedVideoDto = denormalizeVideo(video, UpdatedField.VIDEO_NAME);

        return denormalizedVideoDto;
    }

    public DenormalizedVideoDto denormalizeVideo(Video video, UpdatedField updatedField) {
        DenormalizedVideoDto denormalizedVideoDto = DenormalizedVideoDto.builder()
                .videoId(video.getVideoId())
                .sellerId(video.getUploader().getSellerId())
                .updatedAt(video.getUpdatedAt().toString())
                .build();

        try {
            switch (updatedField) {
                case ADS:
                    List<Ad> ads = video.getAds();
                    denormalizedVideoDto.setVideoAds(ads.stream().map(ad -> {
                        return modelMapper.map(ad, DenormalizedAdDto.class);
                    }).collect(Collectors.toList()));
                    return denormalizedVideoDto;
                case TAGS:
                    List<Tag> tags = video.getVideoTags().stream().map(videoTag -> {
                        return videoTag.getTag();
                    }).collect(Collectors.toList());
                    denormalizedVideoDto.setVideoTags(tags.stream().map(tag -> {
                        return DenormalizedTagDto.builder()
                                .tagId(tag.getTagId())
                                .tagName(tag.getContent())
                                .build();
                    }).collect(Collectors.toList()));
                    return denormalizedVideoDto;
                case THUMBNAIL:
                    VideoUrl videoUrl = video.getVideoUrl();
                    denormalizedVideoDto.setThumbnailUrl(videoUrl.getThumbnailCloudfrontUrl());
                    return denormalizedVideoDto;
                case UPLOADER:
                    Uploader uploader = video.getUploader();
                    denormalizedVideoDto.setSellerId(uploader.getSellerId());
                    denormalizedVideoDto.setSellerName(uploader.getSellerName());
                    denormalizedVideoDto.setSellerLogo(Base64.getEncoder().encodeToString(uploader.getSellerLogo()));
                    return denormalizedVideoDto;
                case VIDEO_NAME:
                    denormalizedVideoDto.setVideoName(video.getVideoName());
                    return denormalizedVideoDto;
                default:
                    throw new IllegalArgumentException("invalid update field");
            }
        }
        catch (IllegalArgumentException e) {
            log.error("invalid update field", e);
            throw new IllegalArgumentException("invalid update field");
        }
        catch (Exception e) {
            log.error("video denormalize error", e);
            throw new RuntimeException("video denormalize error");
        }
    }
}

