package com.travelvcommerce.contentslaveservice.service;

import com.travelvcommerce.contentslaveservice.dto.VideoDto;
import com.travelvcommerce.contentslaveservice.entity.Video;
import com.travelvcommerce.contentslaveservice.repository.VideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DatabaseServiceImpl implements DatabaseService {
    @Autowired
    VideoRepository videoRepository;

    @Override
    public void create(VideoDto videoDto) {
        try {
            videoRepository.save(videoDto.toEntity());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("fail to create video", e);
        }
    }

    @Override
    public void update(String videoId, VideoDto videoDto) {
        try {
            Video video = videoDto.toEntity();
            String id = videoRepository.findByByVideoId(videoId).get().get_id();
            video.set_id(id);
            videoRepository.save(video);
            } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("fail to update video", e);
        }
    }

    @Override
    public void delete(String videoId) {
        try {
            videoRepository.findByByVideoId(videoId).ifPresent(video -> videoRepository.delete(video));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("fail to delete video", e);
        }
    }
}
