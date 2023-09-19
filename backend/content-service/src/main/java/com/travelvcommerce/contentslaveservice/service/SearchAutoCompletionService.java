package com.travelvcommerce.contentslaveservice.service;

import com.travelvcommerce.contentslaveservice.dto.SearchAutoCompletionDto;

public interface SearchAutoCompletionService {
    SearchAutoCompletionDto.SearchAutoCompletionListDto getAutoCompletionList(String searchType, String keyword);
}
