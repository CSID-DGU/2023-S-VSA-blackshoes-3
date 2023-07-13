package com.travelvcommerce.uploadservice.service;

import com.travelvcommerce.uploadservice.entity.Video;
import com.travelvcommerce.uploadservice.repository.VideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VideoDeleteServiceImpl implements VideoDeleteService {
    @Autowired
    private VideoRepository videoRepository;

    @Override
    public String deleteVideo(String userId, String videoId) {
        Video video;
        try {
            video = (Video) videoRepository.findByVideoId(videoId).orElseThrow(() -> new RuntimeException("video not found"));
        } catch (Exception e) {
            log.error("video not found", e);
            throw new RuntimeException("video not found");
        }

        try {
            if (!video.getSellerId().equals(userId)) {
                throw new RuntimeException("sellerId not match");
            }
        } catch (Exception e) {
            log.error("sellerId not match", e);
            throw new RuntimeException("sellerId not match");
        }

        String s3Key = video.getVideoUrl().getVideoS3Url().substring(video.getVideoUrl().getVideoS3Url().indexOf("videos"));

        try {
            videoRepository.delete(video);
        } catch (Exception e) {
            log.error("delete video error", e);
            throw new RuntimeException("delete video error");
        }

        return s3Key;
    }
}
