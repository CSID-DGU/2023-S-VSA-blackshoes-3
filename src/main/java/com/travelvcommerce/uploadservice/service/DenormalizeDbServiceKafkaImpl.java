package com.travelvcommerce.uploadservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;
import com.travelvcommerce.uploadservice.dto.DenormalizedAdDto;
import com.travelvcommerce.uploadservice.dto.DenormalizedTagDto;
import com.travelvcommerce.uploadservice.dto.DenormalizedVideoDto;
import com.travelvcommerce.uploadservice.entity.*;
import com.travelvcommerce.uploadservice.repository.VideoRepository;
import com.travelvcommerce.uploadservice.vo.UpdatedField;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Primary
public class DenormalizeDbServiceKafkaImpl implements DenormalizeDbService {
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private KafkaProducer kafkaProducer;

    public DenormalizedVideoDto denormalizeVideo(String videoId) {
        try {
            Video video = videoRepository.findByVideoId(videoId).orElseThrow(() -> new RuntimeException("video not found"));
            VideoUrl videoUrl = video.getVideoUrl();
            Uploader uploader = video.getUploader();
            List<Tag> tags = video.getVideoTags().stream().map(videoTag -> {
                return videoTag.getTag();
            }).collect(Collectors.toList());
            List<Ad> ads = video.getAds();

            List<DenormalizedTagDto> videoTags = tags.stream().map(tag -> {
                return DenormalizedTagDto.builder()
                        .tagId(tag.getTagId())
                        .tagName(tag.getContent())
                        .build();
            }).collect(Collectors.toList());

            List<DenormalizedAdDto> videoAds = ads.stream().map(ad -> {
                return modelMapper.map(ad, DenormalizedAdDto.class);
            }).collect(Collectors.toList());

            DenormalizedVideoDto denormalizedVideoDto = DenormalizedVideoDto.builder()
                    .videoId(videoId)
                    .videoName(video.getVideoName())
                    .sellerId(uploader.getSellerId())
                    .sellerName(uploader.getSellerName())
                    .sellerLogo(Base64.getEncoder().encodeToString(uploader.getSellerLogo()))
                    .videoUrl(videoUrl.getVideoCloudfrontUrl())
                    .thumbnailUrl(videoUrl.getThumbnailCloudfrontUrl())
                    .createdAt(video.getCreatedAt().toString())
                    .updatedAt(video.getUpdatedAt().toString())
                    .videoTags(videoTags)
                    .videoAds(videoAds)
                    .build();

            return denormalizedVideoDto;
        } catch (Exception e) {
            log.error("denormalizeVideo error", e);
            throw new RuntimeException("denormalizeVideo error");
        }
    }

    public DenormalizedVideoDto denormalizeVideo(String videoId, UpdatedField updatedField) {
        Video video = videoRepository.findByVideoId(videoId).orElseThrow(() -> new RuntimeException("video not found"));
        DenormalizedVideoDto denormalizedVideoDto = DenormalizedVideoDto.builder()
                .videoId(videoId)
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

    @Transactional
    @Override
    public void postDenormalizeData(String videoId) {
        String topic = "video-create";

        DenormalizedVideoDto denormalizedVideoDto = denormalizeVideo(videoId);
        String videoJsonString;
        try {
            videoJsonString = objectMapper.writeValueAsString(denormalizedVideoDto);
        } catch (JsonProcessingException e) {
            log.error("Post denormalize data error", e);
            throw new RuntimeException("Post denormalize data error");
        }

        kafkaProducer.send(topic, videoJsonString);
    }

    @Transactional
    @Override
    public void putDenormalizeData(String videoId, UpdatedField updatedField) {
        String topic = "video-update";

        DenormalizedVideoDto denormalizedVideoDto = denormalizeVideo(videoId, updatedField);
        String videoJsonString;
        try {
            videoJsonString = objectMapper.writeValueAsString(denormalizedVideoDto);
        } catch (JsonProcessingException e) {
            log.error("Post denormalize data error", e);
            throw new RuntimeException("Post denormalize data error");
        }

        kafkaProducer.send(topic, videoJsonString);
    }

    @Override
    public void deleteDenormalizeData(String videoId) {
        String topic = "video-delete";

        kafkaProducer.send(topic, videoId);
    }
}
