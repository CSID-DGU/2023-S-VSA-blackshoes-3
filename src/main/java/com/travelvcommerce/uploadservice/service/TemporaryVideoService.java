package com.travelvcommerce.uploadservice.service;

import com.travelvcommerce.uploadservice.dto.TemporaryVideoDto;
import com.travelvcommerce.uploadservice.vo.S3Video;

import java.util.concurrent.CompletableFuture;

public interface TemporaryVideoService {
    S3Video findTemporaryVideoUrls(String userId, String videoId);
    void deleteTemporaryVideo(String userId, String videoId);
    TemporaryVideoDto.TemporaryVideoResponseDto createTemporaryVideo(String sellerId, String videoId, S3Video videoUrls);
    CompletableFuture checkAndDeleteExpiredVideo(String videoId);
}
