package com.travelvcommerce.uploadservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelvcommerce.uploadservice.dto.*;
import com.travelvcommerce.uploadservice.entity.Video;
import com.travelvcommerce.uploadservice.entity.VideoUrl;
import com.travelvcommerce.uploadservice.service.AwsS3Service;
import com.travelvcommerce.uploadservice.service.DenormalizeDbService;
import com.travelvcommerce.uploadservice.service.VideoUpdateService;
import com.travelvcommerce.uploadservice.vo.S3Thumbnail;
import com.travelvcommerce.uploadservice.vo.UpdatedField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/upload-service")
public class VideoUpdateController {
    @Autowired
    private VideoUpdateService videoUpdateService;
    @Autowired
    private AwsS3Service awsS3Service;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DenormalizeDbService denormalizeDbService;

    @PutMapping("/videos/{userId}/{videoId}/thumbnail")
    public ResponseEntity<ResponseDto> updateThumbnail(@PathVariable("userId") String userId,
                                                       @PathVariable("videoId") String videoId,
                                                       @RequestPart(value = "thumbnail") MultipartFile thumbnail) {
        Video video;
        VideoUrl videoUrl;
        String s3Key;
        S3Thumbnail newS3Thumbnail;
        VideoDto.VideoUpdateResponseDto videoUpdateResponseDto;

        try {
            video = videoUpdateService.getVideo(userId, videoId);
            videoUrl = video.getVideoUrl();
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        try {
            s3Key = videoUpdateService.getThumbnailS3Key(video);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        try {
            newS3Thumbnail = awsS3Service.updateThumbnail(s3Key, thumbnail);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        try {
            videoUpdateResponseDto = videoUpdateService.updateThumbnail(video, videoUrl, newS3Thumbnail);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        try {
            denormalizeDbService.putDenormalizeData(videoId, UpdatedField.THUMBNAIL);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        ResponseDto responseDto = ResponseDto.buildResponseDto(objectMapper.convertValue(videoUpdateResponseDto, Map.class));

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PutMapping("/videos/{userId}/{videoId}/tags")
    public ResponseEntity<ResponseDto> updateTags(@PathVariable("userId") String userId,
                                                  @PathVariable("videoId") String videoId,
                                                  @RequestBody TagDto.TagRequestDto tagRequestDto) {
        VideoDto.VideoUpdateResponseDto videoUpdateResponseDto;

        List<String> tagIdList = tagRequestDto.getTagIds();

        try {
            videoUpdateResponseDto = videoUpdateService.updateTags(userId, videoId, tagIdList);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        try {
            denormalizeDbService.putDenormalizeData(videoId, UpdatedField.TAGS);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        ResponseDto responseDto = ResponseDto.buildResponseDto(objectMapper.convertValue(videoUpdateResponseDto, Map.class));

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PutMapping("/videos/{userId}/{videoId}/ads")
    public ResponseEntity<ResponseDto> updateAds(@PathVariable("userId") String userId,
                                                 @PathVariable("videoId") String videoId,
                                                 @RequestBody AdDto.AdModifyRequestsDto adModifyRequestsDto) {
        VideoDto.VideoUpdateResponseDto videoUpdateResponseDto;

        List<AdDto.AdModifyRequestDto> adModifyRequestDtoList = adModifyRequestsDto.getAdModifyRequests();

        try {
            videoUpdateResponseDto = videoUpdateService.updateAds(userId, videoId, adModifyRequestDtoList);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        try {
            denormalizeDbService.putDenormalizeData(videoId, UpdatedField.ADS);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        ResponseDto responseDto = ResponseDto.buildResponseDto(objectMapper.convertValue(videoUpdateResponseDto, Map.class));

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PutMapping("/videos/{userId}/{videoId}/title")
    public ResponseEntity<ResponseDto> updateVideoName(@PathVariable("userId") String userId,
                                                       @PathVariable("videoId") String videoId,
                                                       @RequestBody VideoDto.VideoNameUpdateRequestDto videoNameUpdateRequestDto) {
        VideoDto.VideoUpdateResponseDto videoUpdateResponseDto;

        String videoName = videoNameUpdateRequestDto.getVideoName();

        try {
            videoUpdateResponseDto = videoUpdateService.updateVideoName(userId, videoId, videoName);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        try {
            denormalizeDbService.putDenormalizeData(videoId, UpdatedField.VIDEO_NAME);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        ResponseDto responseDto = ResponseDto.buildResponseDto(objectMapper.convertValue(videoUpdateResponseDto, Map.class));

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PutMapping("/videos/uploaders/{userId}")
    public ResponseEntity<ResponseDto> updateUploader(@PathVariable("userId") String userId,
                                                      @RequestBody UploaderDto.UploaderModifyRequestDto
                                                              uploaderModifyRequestDto) {
        List<String> updatedVideoIdList;

        try {
            updatedVideoIdList = videoUpdateService.updateUploader(userId, uploaderModifyRequestDto);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        try {
            updatedVideoIdList.forEach(videoId -> denormalizeDbService.putDenormalizeData(videoId, UpdatedField.UPLOADER));
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
