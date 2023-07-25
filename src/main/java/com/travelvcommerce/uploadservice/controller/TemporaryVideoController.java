package com.travelvcommerce.uploadservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelvcommerce.uploadservice.dto.ResponseDto;
import com.travelvcommerce.uploadservice.dto.TemporaryVideoDto;
import com.travelvcommerce.uploadservice.service.TemporaryVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/upload-service")
public class TemporaryVideoController {
    @Autowired
    private TemporaryVideoService temporaryVideoService;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/videos/temporary/{userId}")
    public ResponseEntity<ResponseDto> checkTemporaryVideo(@PathVariable String userId) {
        TemporaryVideoDto.TemporaryVideoResponseDto temporaryVideoResponseDto;

        try {
            temporaryVideoResponseDto = temporaryVideoService.getTemporaryVideo(userId);
        }
        catch (NoSuchElementException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        }
        catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        ResponseDto responseDto = ResponseDto.buildResponseDto(objectMapper.convertValue(temporaryVideoResponseDto, Map.class));

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PutMapping("/videos/temporary/{userId}/{videoId}")
    public ResponseEntity<ResponseDto> extendTemporaryVideoExpiredAt(@PathVariable String userId, @PathVariable String videoId) {
        TemporaryVideoDto.TemporaryVideoResponseDto temporaryVideoResponseDto;

        try {
            temporaryVideoResponseDto = temporaryVideoService.extendTemporaryVideoExpiredAt(userId, videoId);
        }
        catch (NoSuchElementException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        }
        catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        ResponseDto responseDto = ResponseDto.buildResponseDto(objectMapper.convertValue(temporaryVideoResponseDto, Map.class));
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/videos/temporary/{userId}/{videoId}")
    public ResponseEntity<ResponseDto> deleteTemporaryVideo(@PathVariable String userId, @PathVariable String videoId) {
        try {
            temporaryVideoService.deleteTemporaryVideo(userId, videoId);
        }
        catch (NoSuchElementException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        }
        catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
