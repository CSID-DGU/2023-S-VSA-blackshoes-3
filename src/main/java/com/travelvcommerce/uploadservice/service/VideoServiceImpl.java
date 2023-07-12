package com.travelvcommerce.uploadservice.service;

import com.travelvcommerce.uploadservice.dto.VideoDto;
import com.travelvcommerce.uploadservice.entity.Ad;
import com.travelvcommerce.uploadservice.entity.Tag;
import com.travelvcommerce.uploadservice.entity.Video;
import com.travelvcommerce.uploadservice.entity.VideoTag;
import com.travelvcommerce.uploadservice.repository.AdRepository;
import com.travelvcommerce.uploadservice.repository.TagRepository;
import com.travelvcommerce.uploadservice.repository.VideoRepository;
import com.travelvcommerce.uploadservice.repository.VideoTagRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Slf4j
public class VideoServiceImpl implements VideoService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private AdRepository adRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private VideoTagRepository videoTagRepository;

    @Override
//    @Async
    public void uploadVideo(String fileName, MultipartFile videoFile) {
        try {
            String originalFileName = videoFile.getOriginalFilename();
            String uploadFileName = fileName + originalFileName.substring(originalFileName.lastIndexOf("."));
            Path uploadPath = Path.of("src/main/resources/static/videos/tmp");

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(uploadFileName);

            InputStream inputStream = videoFile.getInputStream();
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            inputStream.close();

        } catch (Exception e) {
            log.error("upload video error", e);
            throw new RuntimeException("upload video error");
        }
    }

    @Override
    @Async
    public void encodeVideo(String videoPath) {


    }

    @Override
    @Transactional
    public void saveVideo(String sellerId, VideoDto.VideoUploadRequestDto videoUploadRequestDto, String videoUrl, String thumbnailUrl) {
        Video video = new Video();

        try {
            VideoDto videoDto = VideoDto.builder().
                    videoId(UUID.randomUUID().toString()).
                    videoName(videoUploadRequestDto.getVideoName()).
                    videoUrl(videoUrl).
                    thumbnailUrl(thumbnailUrl).
                    sellerId(sellerId).
                    sellerName(videoUploadRequestDto.getSellerName()).
                    build();

            video = modelMapper.map(videoDto, Video.class);

            videoRepository.save(video);

        } catch (Exception e) {
            log.error("save video error", e);
            throw new RuntimeException("save video error");
        }

        Video savedVideo = video;

        try {
            videoUploadRequestDto.getAdList().forEach(
                    requestAd -> {
                        Ad ad = modelMapper.map(requestAd, Ad.class);
                        ad.setAdId(UUID.randomUUID().toString());
                        ad.setVideo(savedVideo);
                        adRepository.save(ad);
                    }
            );
        } catch (Exception e) {
            log.error("save ad error", e);
            throw new RuntimeException("save ad error");
        }

        try {
            videoUploadRequestDto.getTagIdList().forEach(
                    tagId -> {
                        Tag tag = (Tag) tagRepository.findByTagId(tagId).orElseThrow(() -> new RuntimeException("tag not found"));

                        VideoTag videoTag = new VideoTag();
                        videoTag.setVideo(savedVideo);
                        videoTag.setTag(tag);

                        videoTagRepository.save(videoTag);
                    }
            );
        } catch (Exception e) {
            log.error("save video tag error", e);
            throw new RuntimeException("save video tag error");
        }
    }
}
