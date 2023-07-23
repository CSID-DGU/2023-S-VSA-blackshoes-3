package com.travelvcommerce.uploadservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelvcommerce.uploadservice.dto.TemporaryVideoDto;
import com.travelvcommerce.uploadservice.dto.VideoDto;
import com.travelvcommerce.uploadservice.service.AwsS3Service;
import com.travelvcommerce.uploadservice.service.DenormalizeDbService;
import com.travelvcommerce.uploadservice.service.VideoCreateService;
import com.travelvcommerce.uploadservice.dto.ResponseDto;
import com.travelvcommerce.uploadservice.service.TemporaryVideoService;
import com.travelvcommerce.uploadservice.vo.S3Thumbnail;
import com.travelvcommerce.uploadservice.vo.S3Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/upload-service")
public class VideoCreateController {

    @Autowired
    private AwsS3Service awsS3Service;
    @Autowired
    private VideoCreateService videoCreateService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DenormalizeDbService denormalizeDbService;
    @Autowired
    private TemporaryVideoService temporaryVideoService;

    @GetMapping("/videos/temporary/{userId}")
    public ResponseEntity<ResponseDto> checkTemporaryVideo(@PathVariable String userId) {
        TemporaryVideoDto.TemporaryVideoResponseDto temporaryVideoResponseDto;

        try {
            temporaryVideoResponseDto = temporaryVideoService.getTemporaryVideo(userId);
        }
        catch (NoSuchElementException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        }
        catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        ResponseDto responseDto = ResponseDto.buildResponseDto(objectMapper.convertValue(temporaryVideoResponseDto, Map.class));

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PostMapping("/videos/{userId}")
    public ResponseEntity<ResponseDto> uploadVideo(@PathVariable String userId,
                                                   @RequestPart(value = "video") MultipartFile video) {
        String videoId = UUID.randomUUID().toString();
        String fileName = userId + "_" + videoId;
        String uploadedFilePath;
        String encodedFilePath;
        S3Video videoUrls;
        TemporaryVideoDto.TemporaryVideoResponseDto temporaryVideoResponseDto;

        try {
            uploadedFilePath = videoCreateService.uploadVideo(fileName, video);
            encodedFilePath = videoCreateService.encodeVideo(uploadedFilePath);
            videoUrls = awsS3Service.uploadEncodedVideo(fileName, encodedFilePath);
            temporaryVideoResponseDto = temporaryVideoService.createTemporaryVideo(userId, videoId, videoUrls);
        }
        catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        temporaryVideoService.checkAndDeleteExpiredVideo(videoId);

        ResponseDto responseDto = ResponseDto.buildResponseDto(objectMapper.convertValue(temporaryVideoResponseDto, Map.class));

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PostMapping("/videos/{userId}/{videoId}")
    public ResponseEntity<ResponseDto> createVideo(@PathVariable String userId,
                                                   @PathVariable String videoId,
                                                   @RequestPart(value = "thumbnail") MultipartFile thumbnail,
                                                   @RequestPart(value = "requestUpload")
                                                   VideoDto.VideoUploadRequestDto videoUploadRequestDto) {
        String fileName = userId + "_" + videoId;
        S3Video videoUrls;
        S3Thumbnail thumbnailUrls;
        VideoDto.VideoCreateResponseDto videoCreateResponseDto;

        try {
            videoUrls = temporaryVideoService.findTemporaryVideoUrls(userId, videoId);
            thumbnailUrls = awsS3Service.uploadThumbnail(fileName, thumbnail);
            videoCreateResponseDto = videoCreateService.createVideo(userId, videoId, videoUploadRequestDto, videoUrls, thumbnailUrls);
        }
        catch (NoSuchElementException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        }
        catch (IllegalArgumentException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }
        catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        denormalizeDbService.postDenormalizeData(videoId);
        temporaryVideoService.deleteTemporaryVideo(userId, videoId);

        ResponseDto responseDto = ResponseDto.buildResponseDto(objectMapper.convertValue(videoCreateResponseDto, Map.class));

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
