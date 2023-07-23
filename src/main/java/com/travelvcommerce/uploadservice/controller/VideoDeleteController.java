package com.travelvcommerce.uploadservice.controller;

import com.travelvcommerce.uploadservice.dto.ResponseDto;
import com.travelvcommerce.uploadservice.service.AwsS3Service;
import com.travelvcommerce.uploadservice.service.DenormalizeDbService;
import com.travelvcommerce.uploadservice.service.VideoDeleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/upload-service")
public class VideoDeleteController {
    @Autowired
    AwsS3Service awsS3Service;
    @Autowired
    VideoDeleteService videoDeleteService;
    @Autowired
    DenormalizeDbService denormalizeDbService;

    @DeleteMapping("/videos/{userId}/{videoId}")
    public ResponseEntity<ResponseDto> deleteVideo(@PathVariable(name = "userId") String userId,
                                                   @PathVariable(name = "videoId") String videoId) {

        String s3key;

        try {
            s3key = videoDeleteService.deleteVideo(userId, videoId);
        }
        catch (NoSuchElementException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        }
        catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        try {
            awsS3Service.deleteVideo(s3key);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        denormalizeDbService.deleteDenormalizeData(videoId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
