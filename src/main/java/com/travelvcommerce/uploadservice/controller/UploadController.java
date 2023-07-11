package com.travelvcommerce.uploadservice.controller;

import com.travelvcommerce.uploadservice.dto.TagDto;
import com.travelvcommerce.uploadservice.service.TagService;
import com.travelvcommerce.uploadservice.vo.ResponseTag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping
public class UploadController {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private TagService tagService;

    @GetMapping("/tags")
    public ResponseEntity<List<ResponseTag>> getTags() {

        List<TagDto> tagDtoList = tagService.getAllTags();

        List<ResponseTag> responseTagList = new ArrayList<>();

        tagDtoList.forEach(tagDto -> {
            ResponseTag responseTag = modelMapper.map(tagDto, ResponseTag.class);
            responseTagList.add(responseTag);
        });

        return ResponseEntity.status(HttpStatus.OK).body(responseTagList);
    }

//    @PostMapping("/videos/{userId}")
//    public ResponseEntity<ResponseVideo> uploadVideo(@PathVariable String userId, @RequestPart(value = "file") MultipartFile multipartFile) {
//        return ResponseEntity.status(HttpStatus.OK).body(responseVideo);
//    }
}
