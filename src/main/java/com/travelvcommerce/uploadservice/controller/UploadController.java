package com.travelvcommerce.uploadservice.controller;

import com.travelvcommerce.uploadservice.dto.TagDto;
import com.travelvcommerce.uploadservice.dto.VideoDto;
import com.travelvcommerce.uploadservice.service.AwsS3Service;
import com.travelvcommerce.uploadservice.service.TagService;
import com.travelvcommerce.uploadservice.service.VideoService;
import com.travelvcommerce.uploadservice.dto.ResponseDto;
import com.travelvcommerce.uploadservice.vo.TagTypes;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping
public class UploadController {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private AwsS3Service awsS3Service;
    @Autowired
    private VideoService videoService;

    @GetMapping("/tags")
    public ResponseEntity<ResponseDto> getTags() {

        List<TagDto> tagDtoList = tagService.getAllTags();

        List<TagDto.TagResponseDto> tagResponseDtoList = new ArrayList<>();

        tagDtoList.forEach(tagDto -> {
            TagDto.TagResponseDto tagResponseDto = modelMapper.map(tagDto, TagDto.TagResponseDto.class);
            tagResponseDtoList.add(tagResponseDto);
        });

        ResponseDto responseDto = ResponseDto.builder()
                .payload(Collections.singletonMap("tags", tagResponseDtoList))
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/tags/{type}")
    public ResponseEntity<ResponseDto> getTagsByType(@PathVariable(name = "type") String type) {

        if (!TagTypes.contains(type)) {

            ResponseDto responseDto = ResponseDto.builder()
                    .error("Invalid type")
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        List<TagDto> tagDtoList = tagService.getTagsByType(type);

        List<TagDto.TagResponseDto> tagResponseDtoList = new ArrayList<>();

        tagDtoList.forEach(tagDto -> {
            TagDto.TagResponseDto tagResponseDto = modelMapper.map(tagDto, TagDto.TagResponseDto.class);
            tagResponseDtoList.add(tagResponseDto);
        });

        ResponseDto responseDto = ResponseDto.builder()
                .payload(Collections.singletonMap("tags", tagResponseDtoList))
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PostMapping("/videos/{userId}")
    public ResponseEntity<ResponseDto> uploadVideo(@PathVariable String userId,
                                                   @RequestPart(value = "video") MultipartFile video,
                                                   @RequestPart(value = "thumbnail") MultipartFile thumbnail,
                                                   @RequestPart(value = "requestUpload")
                                                       VideoDto.VideoUploadRequestDto videoUploadRequestDto) {
        String fileName = userId + "_" + videoUploadRequestDto.getVideoName() + "_" + UUID.randomUUID().toString();

        String videoUrl;
        String thumbnailUrl;

        try {
            videoService.uploadVideo(fileName, video);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        try {
            videoUrl = awsS3Service.uploadFile("videos", fileName, video);
            thumbnailUrl = awsS3Service.uploadFile("thumbnail", fileName, thumbnail);
        } catch (IllegalArgumentException e) {
            ResponseDto responseDto = ResponseDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        try {
            videoService.saveVideo(userId, videoUploadRequestDto, videoUrl, thumbnailUrl);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}
