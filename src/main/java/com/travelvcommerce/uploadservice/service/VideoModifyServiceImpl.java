package com.travelvcommerce.uploadservice.service;

import com.travelvcommerce.uploadservice.entity.Video;
import com.travelvcommerce.uploadservice.repository.VideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Slf4j
@Service
public class VideoModifyServiceImpl implements VideoModifyService {
    @Autowired
    private VideoRepository videoRepository;

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
        video.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

        try {
            videoRepository.save(video);
        } catch (Exception e) {
            log.error("update thumbnail error", e);
            throw new RuntimeException("update thumbnail error");
        }
    }
}
