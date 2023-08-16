package com.travelvcommerce.uploadservice.controller;

import com.travelvcommerce.uploadservice.dto.ResponseDto;
import com.travelvcommerce.uploadservice.service.AwsS3Service;
import com.travelvcommerce.uploadservice.service.KafkaVideoInfoProducerService;
import com.travelvcommerce.uploadservice.service.VideoDeleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/upload-service")
public class VideoDeleteController {
    @Autowired
    AwsS3Service awsS3Service;
    @Autowired
    VideoDeleteService videoDeleteService;
    @Autowired
    KafkaVideoInfoProducerService kafkaVideoInfoProducerService;

    @DeleteMapping("/videos/{userId}/{videoId}")
    public ResponseEntity<ResponseDto> deleteVideo(@RequestHeader("Authorization") String id,
                                                   @PathVariable(name = "userId") String userId,
                                                   @PathVariable(name = "videoId") String videoId) {
        if (!id.equals(userId)) {
            ResponseDto responseDto = ResponseDto.buildResponseDto("Invalid id");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseDto);
        }

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

        kafkaVideoInfoProducerService.deleteVideo(videoId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
