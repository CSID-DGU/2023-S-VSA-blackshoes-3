package com.travelvcommerce.contentslaveservice.service;

import java.util.List;

public interface GptRecommendationService {
    List<String> getFiveRecommendedTag(String question);
}
