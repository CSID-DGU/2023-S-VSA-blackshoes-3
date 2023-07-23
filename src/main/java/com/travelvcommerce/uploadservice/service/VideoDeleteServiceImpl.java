package com.travelvcommerce.uploadservice.service;

import com.travelvcommerce.uploadservice.entity.Video;
import com.travelvcommerce.uploadservice.repository.VideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@Slf4j
public class VideoDeleteServiceImpl implements VideoDeleteService {
    @Autowired
    private VideoRepository videoRepository;
    @Value("${video.directory}")
    private String DIRECTORY;

    @Override
    public String deleteVideo(String userId, String videoId) {
        Video video = videoRepository.findByVideoId(videoId).orElseThrow(() -> new NoSuchElementException("video not found"));

        String s3Url = video.getVideoUrl().getVideoS3Url();

        String s3Key = s3Url.substring(s3Url.indexOf(DIRECTORY));

        try {
            videoRepository.delete(video);
        } catch (Exception e) {
            log.error("delete video error", e);
            throw new RuntimeException("delete video error");
        }

        return s3Key;
    }
}
