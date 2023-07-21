package com.travelvcommerce.uploadservice.service;

import com.google.common.net.HttpHeaders;
import com.travelvcommerce.uploadservice.dto.DenormalizedAdDto;
import com.travelvcommerce.uploadservice.dto.DenormalizedTagDto;
import com.travelvcommerce.uploadservice.dto.DenormalizedVideoDto;
import com.travelvcommerce.uploadservice.dto.UploaderDto;
import com.travelvcommerce.uploadservice.entity.*;
import com.travelvcommerce.uploadservice.repository.VideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

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

    @Override
    public DenormalizedVideoDto denormalizeDb(String userId, String videoId) {
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
                .sellerLogo(uploader.getSellerLogo().toString())
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
    }

    @Transactional
    @Override
    public void postDenormalizeData(String userId, String videoId) {

        DenormalizedVideoDto denormalizedVideoDto = denormalizeDb(userId, videoId);

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
            log.error("postDenormalizeData error", e);
            throw new RuntimeException("postDenormalizeData error");
        }
    }

    @Transactional
    @Override
    public void putDenormalizeData(String userId, String videoId) {
        DenormalizedVideoDto denormalizedVideoDto = denormalizeDb(userId, videoId);

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
    public void deleteDenormalizeData(String userId, String videoId) {

    }
}
