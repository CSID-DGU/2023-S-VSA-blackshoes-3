package com.travelvcommerce.contentslaveservice.controller;

import com.travelvcommerce.contentslaveservice.dto.GptDto;
import com.travelvcommerce.contentslaveservice.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("/content-slave-service/gpt")
public class GptController {

    @Value("${chatgpt.api-key}")
    private String chatGptApiKey;

    private final String GPT_URL = "https://api.openai.com/v1/chat/completions";

    @PostMapping("/tag")
    public ResponseEntity<ResponseDto> recommendTag(@RequestBody String question) {
        GptDto.GptRequestDto gptRequestDto = new GptDto.GptRequestDto(question);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + chatGptApiKey);

        HttpEntity<GptDto.GptRequestDto> entity = new HttpEntity<>(gptRequestDto, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<GptDto.GptResponseDto> responseEntity = restTemplate.exchange(GPT_URL, HttpMethod.POST, entity, GptDto.GptResponseDto.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            GptDto.GptResponseDto gptResponseDto = responseEntity.getBody();
            String content = gptResponseDto.getChoices().get(0).getMessage().getContent();

            if (!content.startsWith("[") || !content.endsWith("]")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDto.buildResponseDto("Invalid format: content does not start and end with brackets"));
            }

            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseDto.builder()
                            .payload(Collections.singletonMap("content", content))
                            .build());
        } else {
            return ResponseEntity.status(responseEntity.getStatusCode())
                    .body(ResponseDto.buildResponseDto("Unexpected error occurred"));
        }
    }
}
