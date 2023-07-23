package com.travelvcommerce.uploadservice.service;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DenormalizeDbServiceHttpImpl implements DenormalizeDbService {
    @Value("${api.content-slave-service.url}")
    private String CONTENT_SLAVE_SERVICE_URL;

    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private ModelMapper modelMapper;

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
                    .likes((int) (Math.random() * 100))
                    .views((int) (Math.random() * 100))
                    .adClicks((int) (Math.random() * 100))
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

        DenormalizedVideoDto denormalizedVideoDto = denormalizeVideo(videoId);

        try {
            WebClient webClient = WebClient.builder()
                    .baseUrl(CONTENT_SLAVE_SERVICE_URL)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();
            webClient.post()
                    .uri("/content-slave-service/create")
                    .bodyValue(denormalizedVideoDto)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (Exception e) {
            log.error("post denormalized data error", e);
            throw new RuntimeException("post denormalized data error");
        }
    }

    @Transactional
    @Override
    public void putDenormalizeData(String videoId, UpdatedField updatedField) {
        DenormalizedVideoDto denormalizedVideoDto = denormalizeVideo(videoId, updatedField);

        try {
            WebClient webClient = WebClient.builder()
                    .baseUrl(CONTENT_SLAVE_SERVICE_URL)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();
            webClient.put()
                    .uri("/content-slave-service/update/" + videoId)
                    .bodyValue(denormalizedVideoDto)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (Exception e) {
            log.error("putDenormalizeData error", e);
            throw new RuntimeException("putDenormalizeData error");
        }
    }

    @Override
    public void deleteDenormalizeData(String videoId) {
        try {
            WebClient webClient = WebClient.builder()
                    .baseUrl(CONTENT_SLAVE_SERVICE_URL)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();
            webClient.delete()
                    .uri("/content-slave-service/delete/" + videoId)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (Exception e) {
            log.error("deleteDenormalizeData error", e);
            throw new RuntimeException("deleteDenormalizeData error");
        }
    }
}
