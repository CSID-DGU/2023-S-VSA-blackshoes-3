package com.travelvcommerce.contentslaveservice.service;

import com.travelvcommerce.contentslaveservice.dto.GptDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class GptRecommendationServiceImpl implements GptRecommendationService {
    @Value("${chatgpt.api-key}")
    private String chatGptApiKey;

    private final String GPT_URL = "https://api.openai.com/v1/chat/completions";

    @Override
    public List<String> getFiveRecommendedTag(String question) {
        GptDto.GptRequestDto gptRequestDto = new GptDto.GptRequestDto(question);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + chatGptApiKey);

        HttpEntity<GptDto.GptRequestDto> entity = new HttpEntity<>(gptRequestDto, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<GptDto.GptResponseDto> responseEntity;
        try {
            responseEntity = restTemplate.exchange(GPT_URL, HttpMethod.POST, entity, GptDto.GptResponseDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Error while calling GPT API");

        }

        String content = null;
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            GptDto.GptResponseDto gptResponseDto = responseEntity.getBody();
            content = gptResponseDto.getChoices().get(0).getMessage().getContent();
        }
        if (!content.startsWith("[") || !content.endsWith("]")) {
            throw new RuntimeException("Response does not start and end with brackets");
        }

        List<String> tagNameList = List.of(content.substring(1, content.length() - 1)
                .replaceAll("\'", "").split(", "));

        return tagNameList;
    }
}
