package com.travelvcommerce.uploadservice.service;

import com.travelvcommerce.uploadservice.entity.Video;
import com.travelvcommerce.uploadservice.entity.VideoTag;
import com.travelvcommerce.uploadservice.repository.TagRepository;
import com.travelvcommerce.uploadservice.repository.VideoRepository;
import com.travelvcommerce.uploadservice.repository.VideoTagRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class VideoModifyServiceImpl implements VideoModifyService {
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private VideoTagRepository videoTagRepository;
    @Autowired
    private TagRepository tagRepository;

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
        try {
            video.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            videoRepository.save(video);
        } catch (Exception e) {
            log.error("update thumbnail error", e);
            throw new RuntimeException("update thumbnail error");
        }
    }

    @Override
    @Transactional
    public void updateTag(String userId, String videoId, List<String> tagIdList) {
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

        try {
            video.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            videoRepository.save(video);
        } catch (Exception e) {
            log.error("update video tag error", e);
            throw new RuntimeException("update video tag error");
        }
    }
}
