package com.travelvcommerce.contentslaveservice.service;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GptRecommendationServiceImplTest {

    @Test
    void getFiveRecommendedTag() {
        GptRecommendationServiceImpl gptRecommendationService = new GptRecommendationServiceImpl();
        String question = "I want to go to the beach";
        List<String> result = gptRecommendationService.getFiveRecommendedTag(question);
        result.stream().forEach(System.out::print);
    }
}