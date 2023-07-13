package com.travelvcommerce.uploadservice.controller;

import com.travelvcommerce.uploadservice.dto.AdDto;
import com.travelvcommerce.uploadservice.dto.ResponseDto;
import com.travelvcommerce.uploadservice.dto.TagDto;
import com.travelvcommerce.uploadservice.entity.Video;
import com.travelvcommerce.uploadservice.service.AwsS3Service;
import com.travelvcommerce.uploadservice.service.VideoModifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload-service")
public class ModifyController {
    @Autowired
    private VideoModifyService videoModifyService;
    @Autowired
    private AwsS3Service awsS3Service;

    @PutMapping("/videos/{userId}/{videoId}/thumbnail")
    public ResponseEntity<ResponseDto> modifyThumbnail(@PathVariable("userId") String userId, @PathVariable("videoId") String videoId, @RequestPart(value = "thumbnail") MultipartFile thumbnail) {
        Video video;
        String s3Key;

        try {
            video = videoModifyService.getVideo(userId, videoId);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        try {
            s3Key = videoModifyService.getThumbnailS3Key(video);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        try {
            awsS3Service.updateThumbnail(s3Key, thumbnail);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        try {
            videoModifyService.updateThumbnail(video);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PutMapping("/videos/{userId}/{videoId}/tags")
    public ResponseEntity<ResponseDto> modifyTags(@PathVariable("userId") String userId, @PathVariable("videoId") String videoId, @RequestBody TagDto.TagRequestDto tagRequestDto) {
        return null;
    }

    @PutMapping("/videos/{userId}/{videoId}/ads")
    public ResponseEntity<ResponseDto> modifyAds(@PathVariable("userId") String userId, @PathVariable("videoId") String videoId, @RequestBody AdDto.AdRequestsDto adRequestsDto) {
        return null;
    }
}
