package com.travelvcommerce.uploadservice.controller;

import com.travelvcommerce.uploadservice.dto.VideoDto;
import com.travelvcommerce.uploadservice.service.AwsS3Service;
import com.travelvcommerce.uploadservice.service.VideoCreateService;
import com.travelvcommerce.uploadservice.dto.ResponseDto;
import com.travelvcommerce.uploadservice.vo.S3Thumbnail;
import com.travelvcommerce.uploadservice.vo.S3Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/videos/{userId}")
    public ResponseEntity<ResponseDto> createVideo(@PathVariable String userId,
                                                   @RequestPart(value = "video") MultipartFile video,
                                                   @RequestPart(value = "thumbnail") MultipartFile thumbnail,
                                                   @RequestPart(value = "requestUpload")
                                                       VideoDto.VideoUploadRequestDto videoUploadRequestDto) {
        String videoId = UUID.randomUUID().toString();
        String fileName = userId + "_" + videoUploadRequestDto.getVideoName() + "_" + videoId;

        String uploadedFilePath;
        String encodedFilePath;
        S3Video videoUrls;
        S3Thumbnail thumbnailUrls;

        try {
            uploadedFilePath = videoCreateService.uploadVideo(fileName, video);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        try {
            encodedFilePath = videoCreateService.encodeVideo(uploadedFilePath);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        try {
            videoUrls = awsS3Service.uploadEncodedVideo(fileName, encodedFilePath);
            thumbnailUrls = awsS3Service.uploadThumbnail(fileName, thumbnail);
        } catch (Exception e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        try {
            videoCreateService.createVideo(userId, videoId, videoUploadRequestDto, videoUrls, thumbnailUrls);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            awsS3Service.deleteVideo(videoUrls.getS3Url().substring(videoUrls.getS3Url().indexOf("videos")));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}
