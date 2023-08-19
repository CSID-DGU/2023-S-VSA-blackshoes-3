package com.travelvcommerce.uploadservice.controller;

import com.travelvcommerce.uploadservice.dto.ResponseDto;
import com.travelvcommerce.uploadservice.dto.TagDto;
import com.travelvcommerce.uploadservice.service.TagService;
import com.travelvcommerce.uploadservice.vo.TagType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/upload-service")
public class TagController {
    private final ModelMapper modelMapper;
    private final TagService tagService;

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

        if (!TagType.contains(type)) {

            ResponseDto responseDto = ResponseDto.buildResponseDto("invalid tag type");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        List<TagDto> tagDtoList = tagService.getTagsByType(type);

        List<TagDto.TagResponseDto> tagResponseDtoList = new ArrayList<>();

        tagDtoList.forEach(tagDto -> {
            TagDto.TagResponseDto tagResponseDto = modelMapper.map(tagDto, TagDto.TagResponseDto.class);
            tagResponseDtoList.add(tagResponseDto);
        });

        ResponseDto responseDto = ResponseDto.buildResponseDto(Collections.singletonMap("tags", tagResponseDtoList));

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
