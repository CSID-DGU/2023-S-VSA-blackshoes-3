package com.travelvcommerce.uploadservice.service;

import com.travelvcommerce.uploadservice.dto.*;
import com.travelvcommerce.uploadservice.entity.*;
import com.travelvcommerce.uploadservice.repository.*;
import com.travelvcommerce.uploadservice.vo.S3Thumbnail;
import com.travelvcommerce.uploadservice.vo.S3Video;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Primary
@Service
@Slf4j
public class VideoCreateServiceImpl implements VideoCreateService {
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
    private UploaderRepository uploaderRepository;
    @Autowired
    private FFmpegWrapper ffmpegWrapper;
    @Value("${video.encoding-resolutions}")
    private String ENCODING_RESOLUTIONS;

    @Override
    public String uploadVideo(String userId, String videoId, MultipartFile videoFile) {

        uploaderRepository.findBySellerId(userId).orElseThrow(() -> new NoSuchElementException("uploader not found"));

        String fileName = videoId;

        try {
            String originalFileName = videoFile.getOriginalFilename();
            String uploadFileName = fileName + originalFileName.substring(originalFileName.lastIndexOf("."));
            Path uploadPath = Path.of("src/main/resources/static/videos/original/" + userId);

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
    public String encodeVideo(String userId, String videoId, String filePath) {
        String inputPath = filePath;

        Path encodingPath = Path.of("src/main/resources/static/videos/encoded/" + userId + "/" + videoId);

        try {
            if (!Files.exists(encodingPath)) {
                Files.createDirectories(encodingPath);
            }
            List<Integer> resolutionList = Arrays.stream(ENCODING_RESOLUTIONS.split(",")).map(Integer::parseInt).collect(Collectors.toList());

            resolutionList.stream().forEach(resolution -> {
                try {
                    log.info("encoding video to resolution: " + resolution);
                    ffmpegWrapper.encodeToHls(userId, inputPath, encodingPath.toString(), resolutionList.indexOf(resolution), resolution);
                } catch (IOException e) {
                    log.error("encode video error", e);
                    throw new RuntimeException(e);
                }
            });

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
    public DenormalizedVideoDto createVideo(String sellerId, String videoId,
                                            VideoDto.VideoUploadRequestDto videoUploadRequestDto,
                                            S3Video videoUrls, S3Thumbnail thumbnailUrls) {
        Uploader uploader;

        uploader = uploaderRepository.findBySellerId(sellerId).orElseThrow(() -> new NoSuchElementException("uploader not found"));

        Video savedVideo = saveAndGetVideo(videoId, videoUploadRequestDto, uploader);

        saveVideoUrl(videoUrls, thumbnailUrls, savedVideo);

        saveAds(videoUploadRequestDto, savedVideo);

        saveVideoTags(videoUploadRequestDto, savedVideo);

        try {
            return denormalizeVideo(savedVideo);
        } catch (Exception e) {
            log.error("map video to videoDto error", e);
            throw new RuntimeException("map video to videoDto error");
        }
    }

    private Video saveAndGetVideo(String videoId, VideoDto.VideoUploadRequestDto videoUploadRequestDto, Uploader uploader) {
        Video video;

        try {
            VideoDto videoDto = VideoDto.builder().
                    videoId(videoId).
                    videoName(videoUploadRequestDto.getVideoName()).
                    build();

            video = modelMapper.map(videoDto, Video.class);

            video.setUploader(uploader);
            video.setVideoTags(new ArrayList<>());
            video.setAds(new ArrayList<>());

            videoRepository.save(video);
        } catch (Exception e) {
            log.error("save video error", e);
            throw new RuntimeException("save video error");
        }

        return video;
    }

    private void saveVideoUrl(S3Video videoUrls, S3Thumbnail thumbnailUrls, Video savedVideo) {
        try {
            VideoUrlDto videoUrlDto = VideoUrlDto.builder().
                    videoS3Url(videoUrls.getS3Url()).
                    videoCloudfrontUrl(videoUrls.getCloudfrontUrl()).
                    thumbnailS3Url(thumbnailUrls.getS3Url()).
                    thumbnailCloudfrontUrl(thumbnailUrls.getCloudfrontUrl()).
                    build();

            VideoUrl videoUrl = modelMapper.map(videoUrlDto, VideoUrl.class);
            videoUrlRepository.save(videoUrl);
            savedVideo.setVideoUrl(videoUrl);

        } catch (Exception e) {
            log.error("save video url error", e);
            throw new RuntimeException("save video url error");
        }
    }

    private void saveAds(VideoDto.VideoUploadRequestDto videoUploadRequestDto, Video savedVideo) {
        try {
            videoUploadRequestDto.getAdList().forEach(
                    requestAd -> {
                        Ad ad = modelMapper.map(requestAd, Ad.class);
                        ad.setAdId(UUID.randomUUID().toString());
                        ad.setVideo(savedVideo);
                        adRepository.save(ad);

                        savedVideo.getAds().add(ad);
                    }
            );
        } catch (Exception e) {
            log.error("save ad error", e);
            throw new RuntimeException("save ad error");
        }
    }

    private void saveVideoTags(VideoDto.VideoUploadRequestDto videoUploadRequestDto, Video savedVideo) {
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

                        savedVideo.getVideoTags().add(videoTag);
                    }
            );
        } catch (Exception e) {
            log.error("save video tag error", e);
            throw new RuntimeException("save video tag error");
        }
    }

    public DenormalizedVideoDto denormalizeVideo(Video video) {
        try {
            VideoUrl videoUrl = video.getVideoUrl();
            Uploader uploader = video.getUploader();
            List<Tag> tags = video.getVideoTags().stream().map(videoTag -> {
                return videoTag.getTag();
            }).collect(Collectors.toList());
            List<Ad> ads = video.getAds();

            List<DenormalizedTagDto> videoTags = tags.stream().map(tag -> {
                return DenormalizedTagDto.builder()
                        .tagId(tag.getTagId())
                        .tagName(tag.getContent())
                        .build();
            }).collect(Collectors.toList());

            List<DenormalizedAdDto> videoAds = ads.stream().map(ad -> {
                return modelMapper.map(ad, DenormalizedAdDto.class);
            }).collect(Collectors.toList());

            DenormalizedVideoDto denormalizedVideoDto = DenormalizedVideoDto.builder()
                    .videoId(video.getVideoId())
                    .videoName(video.getVideoName())
                    .sellerId(uploader.getSellerId())
                    .sellerName(uploader.getSellerName())
                    .sellerLogo(Base64.getEncoder().encodeToString(uploader.getSellerLogo()))
                    .videoUrl(videoUrl.getVideoCloudfrontUrl())
                    .thumbnailUrl(videoUrl.getThumbnailCloudfrontUrl())
                    .createdAt(video.getCreatedAt().toString())
                    .updatedAt(video.getUpdatedAt().toString())
                    .videoTags(videoTags)
                    .videoAds(videoAds)
                    .build();

            return denormalizedVideoDto;
        } catch (Exception e) {
            log.error("denormalizeVideo error", e);
            throw new RuntimeException("denormalizeVideo error");
        }
    }
}
