package com.travelvcommerce.contentslaveservice.service;

import com.travelvcommerce.contentslaveservice.dto.SearchAutoCompletionDto;
import com.travelvcommerce.contentslaveservice.repository.VideoRepository;
import com.travelvcommerce.contentslaveservice.vo.UserSearchType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SearchAutoCompletionServiceImpl implements SearchAutoCompletionService {
    @Autowired
    private VideoRepository videoRepository;

    @Override
    public SearchAutoCompletionDto.SearchAutoCompletionListDto getAutoCompletionList(String searchType, String keyword) {
        searchType = searchType.toUpperCase();
        SearchAutoCompletionDto.SearchAutoCompletionListDto searchAutoCompletionListDto;

        if (searchType.equals(UserSearchType.SELLERNAME.toString())) {
            List<SearchAutoCompletionDto> searchAutoCompletionDtoList = videoRepository.findAutoCompleteResultsBySellerName(keyword);

            List<String> autoCompletionList = searchAutoCompletionDtoList.stream()
                    .map(SearchAutoCompletionDto::getSellerName)
                    .collect(Collectors.toList());

            searchAutoCompletionListDto = SearchAutoCompletionDto.SearchAutoCompletionListDto.builder().autoCompletionList(autoCompletionList).build();
        }
        if (searchType.equals(UserSearchType.VIDEONAME.toString())) {
            List<SearchAutoCompletionDto> searchAutoCompletionDtoList = videoRepository.findAutoCompleteResultsByVideoName(keyword);

            List<String> autoCompletionList = searchAutoCompletionDtoList.stream()
                    .map(SearchAutoCompletionDto::getVideoName)
                    .collect(Collectors.toList());

            searchAutoCompletionListDto = SearchAutoCompletionDto.SearchAutoCompletionListDto.builder().autoCompletionList(autoCompletionList).build();
        }
        else {
            throw new IllegalArgumentException("Invalid search type");
        }

        return searchAutoCompletionListDto;
    }
}
