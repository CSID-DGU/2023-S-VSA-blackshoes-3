package com.tavelvcommerce.commentservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tavelvcommerce.commentservice.dto.CommentDto;
import com.tavelvcommerce.commentservice.dto.ResponseDto;
import com.tavelvcommerce.commentservice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comment-service")
public class CommentController {
    private final ObjectMapper objectMapper;
    private final CommentService commentService;

    @PostMapping("/comments/{videoId}/{userId}")
    public ResponseEntity<ResponseDto> createComment(@PathVariable(name = "videoId") String videoId,
                                                     @PathVariable(name = "userId") String userId,
                                                     @RequestBody CommentDto.CommentRequestDto commentRequestDto) {
        String commentId = UUID.randomUUID().toString();
        String content = commentRequestDto.getContent();

        CommentDto.CommentCreateResponseDto commentCreateResponseDto;
        try {
            commentCreateResponseDto = commentService.createComment(commentId, videoId, userId, content);
        } catch (IllegalArgumentException e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        ResponseDto responseDto = ResponseDto.builder().payload(objectMapper.convertValue(commentCreateResponseDto, Map.class)).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
