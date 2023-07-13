package com.travelvcommerce.uploadservice.controller;

import com.travelvcommerce.uploadservice.dto.ResponseDto;
import com.travelvcommerce.uploadservice.service.AwsS3Service;
import com.travelvcommerce.uploadservice.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/upload-service")
public class DeleteController {
    @Autowired
    AwsS3Service awsS3Service;
    @Autowired
    VideoService videoService;

    @DeleteMapping("/videos/{userId}/{videoId}")
    public ResponseEntity<ResponseDto> deleteVideo(@PathVariable(name = "userId") String userId,
                                                   @PathVariable(name = "videoId") String videoId) {

        String s3key;

        try {
            s3key = videoService.deleteVideo(userId, videoId);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        try {
            awsS3Service.deleteVideo(s3key);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
