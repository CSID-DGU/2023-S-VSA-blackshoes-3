package com.travelvcommerce.uploadservice.service;

import com.travelvcommerce.uploadservice.dto.TemporaryVideoDto;
import com.travelvcommerce.uploadservice.entity.TemporaryVideo;
import com.travelvcommerce.uploadservice.repository.TemporaryVideoRepository;
import com.travelvcommerce.uploadservice.repository.VideoRepository;
import com.travelvcommerce.uploadservice.vo.S3Video;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class TemporaryVideoServiceImpl implements TemporaryVideoService {
    @Autowired
    private TemporaryVideoRepository temporaryVideoRepository;
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private AwsS3Service awsS3Service;
    @Autowired
    private ModelMapper modelMapper;

    @Value("${video.ttl}")
    private int VIDEO_TTL;
    @Value("${video.directory}")
    private String DIRECTORY;

    @Override
    public TemporaryVideoDto.TemporaryVideoResponseDto getTemporaryVideo(String userId) {
        TemporaryVideoDto.TemporaryVideoResponseDto temporaryVideoResponseDto;
        TemporaryVideo temporaryVideo = temporaryVideoRepository.findBySellerId(userId)
                .orElseThrow(() -> new NoSuchElementException("temporary video not found"));

        temporaryVideoResponseDto = modelMapper.map(temporaryVideo, TemporaryVideoDto.TemporaryVideoResponseDto.class);

        return temporaryVideoResponseDto;
    }

    @Override
    @Transactional
    public TemporaryVideoDto.TemporaryVideoResponseDto createTemporaryVideo(String sellerId, String videoId, S3Video videoUrls) {
        TemporaryVideoDto.TemporaryVideoResponseDto temporaryVideoResponseDto;

        Timestamp uploadedAt = new Timestamp(System.currentTimeMillis());
        Timestamp expiredAt = new Timestamp(System.currentTimeMillis() + VIDEO_TTL);

        TemporaryVideoDto temporaryVideoDto = TemporaryVideoDto.builder()
                .videoId(videoId)
                .sellerId(sellerId)
                .videoS3Url(videoUrls.getS3Url())
                .videoCloudfrontUrl(videoUrls.getCloudfrontUrl())
                .uploadedAt(uploadedAt.toString())
                .expiredAt(expiredAt.toString())
                .build();

        TemporaryVideo temporaryVideo = modelMapper.map(temporaryVideoDto, TemporaryVideo.class);

        try {
            temporaryVideoRepository.save(temporaryVideo);
        } catch (Exception e) {
            log.error("fail to create temporary video", e);
            throw new RuntimeException("fail to create temporary video");
        }

        temporaryVideoResponseDto = modelMapper.map(temporaryVideo, TemporaryVideoDto.TemporaryVideoResponseDto.class);

        return temporaryVideoResponseDto;
    }

    @Override
    public S3Video findTemporaryVideoUrls(String userId, String videoId) {
        TemporaryVideo temporaryVideo = temporaryVideoRepository.findBySellerIdAndVideoId(userId, videoId)
                .orElseThrow(() -> new NoSuchElementException("temporary video not found"));

        S3Video s3Urls = S3Video.builder()
                .s3Url(temporaryVideo.getVideoS3Url())
                .cloudfrontUrl(temporaryVideo.getVideoCloudfrontUrl())
                .build();

        return s3Urls;
    }

    @Override
    public void deleteTemporaryVideo(String userId, String videoId) {
        TemporaryVideo temporaryVideo = temporaryVideoRepository.findBySellerIdAndVideoId(userId, videoId)
                .orElseThrow(() -> new NoSuchElementException("temporary video not found"));

        try {
            temporaryVideoRepository.delete(temporaryVideo);
        } catch (Exception e) {
            log.error("fail to delete temporary video", e);
            throw new RuntimeException("fail to delete temporary video");
        }
    }

    @Override
    @Async
    public CompletableFuture checkAndDeleteExpiredVideo(String videoId) {
        try {
            Thread.sleep(VIDEO_TTL);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        TemporaryVideo temporaryVideo;
        String s3Key;

        temporaryVideo = temporaryVideoRepository.findByVideoId(videoId).orElseThrow(() -> new NoSuchElementException("temporary video not found"));
        if (temporaryVideo.getExpiredAt().before(new Timestamp(System.currentTimeMillis()))) {
            String s3Url = temporaryVideo.getVideoS3Url();
            s3Key = s3Url.substring(s3Url.indexOf(DIRECTORY));
            temporaryVideoRepository.delete(temporaryVideo);
            awsS3Service.deleteVideo(s3Key);
        }

        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Transactional
    public TemporaryVideoDto.TemporaryVideoResponseDto extendTemporaryVideoExpiredAt(String userId, String videoId) {
        TemporaryVideo temporaryVideo = temporaryVideoRepository.findBySellerIdAndVideoId(userId, videoId)
                .orElseThrow(() -> new NoSuchElementException("temporary video not found"));

        Timestamp expiredAt = new Timestamp(System.currentTimeMillis() + VIDEO_TTL);
        temporaryVideo.setExpiredAt(expiredAt);

        TemporaryVideoDto.TemporaryVideoResponseDto temporaryVideoResponseDto =
                modelMapper.map(temporaryVideo, TemporaryVideoDto.TemporaryVideoResponseDto.class);

        return temporaryVideoResponseDto;
    }

    @Override
    @Transactional
    public void findAllExpiredVideoAndDelete() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        List<TemporaryVideo> expiredTemporaryVideos = temporaryVideoRepository.findAllByExpiredVideo(now);
        expiredTemporaryVideos.stream().forEach(temporaryVideo -> {
            String s3Url = temporaryVideo.getVideoS3Url();
            String s3Key = s3Url.substring(s3Url.indexOf(DIRECTORY));
            temporaryVideoRepository.delete(temporaryVideo);
            awsS3Service.deleteVideo(s3Key);
        });
    }
}
