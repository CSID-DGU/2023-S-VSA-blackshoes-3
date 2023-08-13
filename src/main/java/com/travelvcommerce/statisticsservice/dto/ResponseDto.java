package com.travelvcommerce.statisticsservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(builder = ResponseDto.ResponseDtoBuilder.class)
public class ResponseDto {
    private Map<String, Object> payload;
    private String error;


    public static ResponseDto buildResponseDto(Map<String, Object> payload, String error) {
        return ResponseDto.builder()
                .payload(payload)
                .error(error)
                .build();
    }

    public static ResponseDto buildResponseDto(Map<String, Object> payload) {
        return ResponseDto.builder()
                .payload(payload)
                .build();
    }

    public static ResponseDto buildResponseDto(String error) {
        return ResponseDto.builder()
                .error(error)
                .build();
    }
}
