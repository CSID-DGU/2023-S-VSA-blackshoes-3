package com.travelvcommerce.uploadservice.service;

import com.travelvcommerce.uploadservice.dto.VideoDto;
import com.travelvcommerce.uploadservice.dto.VideoUrlDto;
import com.travelvcommerce.uploadservice.entity.*;
import com.travelvcommerce.uploadservice.repository.*;
import com.travelvcommerce.uploadservice.vo.S3Thumbnail;
import com.travelvcommerce.uploadservice.vo.S3Video;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Slf4j
public class VideoUploadServiceImpl implements VideoUploadService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private VideoUrlRepository videoUrlRepository;
    @Autowired
    private AdRepository adRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private VideoTagRepository videoTagRepository;
    @Autowired
    private FFmpegWrapper ffmpegWrapper;

    @Override
    public String uploadVideo(String fileName, MultipartFile videoFile) {
        try {
            String originalFileName = videoFile.getOriginalFilename();
            String uploadFileName = fileName + originalFileName.substring(originalFileName.lastIndexOf("."));
            Path uploadPath = Path.of("src/main/resources/static/videos/original");

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(uploadFileName);

            InputStream inputStream = videoFile.getInputStream();
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            inputStream.close();

            return filePath.toString();

        } catch (Exception e) {
            log.error("upload video error", e);
            throw new RuntimeException("upload video error");
        }
    }

    @Override
    public String encodeVideo(String filePath) {
        String inputPath = filePath;

        String fileName = new File(filePath).getName();
        fileName = fileName.substring(0, fileName.lastIndexOf("."));

        Path encodingPath = Path.of("src\\main\\resources\\static\\videos\\encoded\\" + fileName);

        try {
            if (!Files.exists(encodingPath)) {
                Files.createDirectories(encodingPath);
            }

            String outputPath = encodingPath + "\\" + fileName + ".m3u8";
            ffmpegWrapper.encodeToHls(inputPath, outputPath);

            File file = new File(inputPath);
            file.delete();

            return encodingPath.toString();

        } catch (Exception e) {
            log.error("encode video error", e);
            File originalfile = new File(inputPath);
            originalfile.delete();
            deleteFolder(encodingPath.toString());
            throw new RuntimeException("encode video error");
        }
    }

    private static void deleteFolder(String filePath) {
        File encodingPathFile = new File(filePath.toString());
        File[] encodingPathFileList = encodingPathFile.listFiles();
        for (File file : encodingPathFileList) {
            file.delete();
        }
        encodingPathFile.delete();
    }

    @Override
    @Transactional
    public void saveVideo(String sellerId, String videoId,
                          VideoDto.VideoUploadRequestDto videoUploadRequestDto,
                          S3Video videoUrls, S3Thumbnail thumbnailUrl) {
        Video video = new Video();

        try {
            VideoDto videoDto = VideoDto.builder().
                    videoId(videoId).
                    videoName(videoUploadRequestDto.getVideoName()).
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
            VideoUrlDto videoUrlDto = VideoUrlDto.builder().
                    videoS3Url(videoUrls.getS3Url()).
                    videoCloudfrontUrl(videoUrls.getCloudFrontUrl()).
                    thumbnailS3Url(thumbnailUrl.getS3Url()).
                    thumbnailCloudfrontUrl(thumbnailUrl.getCloudFrontUrl()).
                    build();

            VideoUrl videoUrl = modelMapper.map(videoUrlDto, VideoUrl.class);
            videoUrlRepository.save(videoUrl);
            savedVideo.setVideoUrl(videoUrl);

        } catch (Exception e) {
            log.error("save video url error", e);
            throw new RuntimeException("save video url error");
        }

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
                        Tag tag;
                        try {
                            tag = tagRepository.findByTagId(tagId).orElseThrow(() -> new RuntimeException("tag not found"));
                        } catch (Exception e) {
                            log.error("tag not found", e);
                            throw new RuntimeException("tag not found");
                        }
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
