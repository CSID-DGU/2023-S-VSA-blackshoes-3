package com.travelvcommerce.contentslaveservice.service;

import com.travelvcommerce.contentslaveservice.dto.VideoDto;
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
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void update(String videoId, VideoDto videoDto) {

    }

    @Override
    public void delete(String videoId, VideoDto videoDto) {

    }
}
