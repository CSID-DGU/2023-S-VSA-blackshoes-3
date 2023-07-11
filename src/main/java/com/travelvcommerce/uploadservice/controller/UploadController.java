package com.travelvcommerce.uploadservice.controller;

import com.travelvcommerce.uploadservice.dto.TagDto;
import com.travelvcommerce.uploadservice.service.AwsS3Service;
import com.travelvcommerce.uploadservice.service.TagService;
import com.travelvcommerce.uploadservice.service.VideoService;
import com.travelvcommerce.uploadservice.vo.RequestUpload;
import com.travelvcommerce.uploadservice.vo.ResponseBody;
import com.travelvcommerce.uploadservice.vo.ResponseTag;
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
    public ResponseEntity<ResponseBody> getTags() {

        List<TagDto> tagDtoList = tagService.getAllTags();

        List<ResponseTag> responseTagList = new ArrayList<>();

        tagDtoList.forEach(tagDto -> {
            ResponseTag responseTag = modelMapper.map(tagDto, ResponseTag.class);
            responseTagList.add(responseTag);
        });

        ResponseBody responseBody = ResponseBody.builder()
                .payload(Collections.singletonMap("tags", responseTagList))
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @GetMapping("/tags/{type}")
    public ResponseEntity<ResponseBody> getTagsByType(@PathVariable(name = "type") String type) {

        if (!TagTypes.contains(type)) {

            ResponseBody responseBody = ResponseBody.builder()
                    .error("Invalid type")
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }

        List<TagDto> tagDtoList = tagService.getTagsByType(type);

        List<ResponseTag> responseTagList = new ArrayList<>();

        tagDtoList.forEach(tagDto -> {
            ResponseTag responseTag = modelMapper.map(tagDto, ResponseTag.class);
            responseTagList.add(responseTag);
        });

        ResponseBody responseBody = ResponseBody.builder()
                .payload(Collections.singletonMap("tags", responseTagList))
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @PostMapping("/videos/{userId}")
    public ResponseEntity<ResponseBody> uploadVideo(@PathVariable String userId,
                                                    @RequestPart(value = "video") MultipartFile video,
                                                    @RequestPart(value = "thumbnail") MultipartFile thumbnail,
                                                    @RequestPart(value = "requestUpload") RequestUpload requestUpload) {
        String fileName = userId + "_" + requestUpload.getVideoName() + "_" + UUID.randomUUID().toString();

        String videoUrl;
        String thumbnailUrl;

        try {
            videoUrl = awsS3Service.uploadFile("videos", fileName, video);
            thumbnailUrl = awsS3Service.uploadFile("thumbnail", fileName, thumbnail);
        } catch (IllegalArgumentException e) {
            ResponseBody responseBody = ResponseBody.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        } catch (RuntimeException e) {
            ResponseBody responseBody = ResponseBody.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }

        try {
            videoService.saveVideo(userId, requestUpload, videoUrl, thumbnailUrl);
        } catch (RuntimeException e) {
            ResponseBody responseBody = ResponseBody.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}
