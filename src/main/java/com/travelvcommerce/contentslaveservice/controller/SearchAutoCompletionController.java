package com.travelvcommerce.contentslaveservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelvcommerce.contentslaveservice.dto.ResponseDto;
import com.travelvcommerce.contentslaveservice.dto.SearchAutoCompletionDto;
import com.travelvcommerce.contentslaveservice.service.SearchAutoCompletionService;
import com.travelvcommerce.contentslaveservice.vo.UserSearchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/content-slave-service")
public class SearchAutoCompletionController {
    @Autowired
    private SearchAutoCompletionService searchAutoCompletionService;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/auto-completion")
    public ResponseEntity<ResponseDto> getAutoCompletion(@RequestParam(name = "searchType") String searchType,
                                                         @RequestParam(name = "keyword") String keyword) {
        try {
            if (!UserSearchType.contains(searchType.toUpperCase())) {
                throw new IllegalArgumentException("Invalid search type");
            }
        } catch (IllegalArgumentException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        SearchAutoCompletionDto.SearchAutoCompletionListDto searchAutoCompletionListDto;
        try {
            searchAutoCompletionListDto = searchAutoCompletionService.getAutoCompletionList(searchType, keyword);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        ResponseDto responseDto = ResponseDto.buildResponseDto(objectMapper.convertValue(searchAutoCompletionListDto, Map.class));

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
