package com.travelvcommerce.contentslaveservice.service;

import com.travelvcommerce.contentslaveservice.dto.SearchAutoCompletionDto;
import com.travelvcommerce.contentslaveservice.repository.VideoRepository;
import com.travelvcommerce.contentslaveservice.vo.UserSearchType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchAutoCompletionServiceImpl implements SearchAutoCompletionService {
    private final VideoRepository videoRepository;
    private final StringRedisTemplate redisTemplate;

    @Override
    public SearchAutoCompletionDto.SearchAutoCompletionListDto getAutoCompletionList(String searchType, String keyword) {
        String autoCompletionKey = "autoCompletion:" + searchType + ":" + keyword;
        List<String> autoCompletionList;

        try {
            autoCompletionList = redisTemplate.opsForList().range(autoCompletionKey, 0, -1);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Redis read error");
        }

        if (autoCompletionList != null && autoCompletionList.size() > 0) {
            return SearchAutoCompletionDto.SearchAutoCompletionListDto.builder().autoCompletionList(autoCompletionList).build();
        }

        searchType = searchType.toUpperCase();
        SearchAutoCompletionDto.SearchAutoCompletionListDto searchAutoCompletionListDto;

        if (searchType.equals(UserSearchType.SELLERNAME.toString())) {
            List<SearchAutoCompletionDto> searchAutoCompletionDtoList = videoRepository.findAutoCompleteResultsBySellerName(keyword);

            autoCompletionList = searchAutoCompletionDtoList.stream()
                    .map(SearchAutoCompletionDto::getSellerName)
                    .collect(Collectors.toList());
        }
        else if (searchType.equals(UserSearchType.VIDEONAME.toString())) {
            List<SearchAutoCompletionDto> searchAutoCompletionDtoList = videoRepository.findAutoCompleteResultsByVideoName(keyword);

            autoCompletionList = searchAutoCompletionDtoList.stream()
                    .map(SearchAutoCompletionDto::getVideoName)
                    .collect(Collectors.toList());
        }
        else {
            log.error("Invalid search type");
            throw new IllegalArgumentException("Invalid search type");
        }

        try {
            if (autoCompletionList != null && autoCompletionList.size() > 0) {
                redisTemplate.opsForList().leftPushAll(autoCompletionKey, autoCompletionList);
                redisTemplate.expire(autoCompletionKey, 60 * 60 * 24, java.util.concurrent.TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Redis save error");
        }

        searchAutoCompletionListDto = SearchAutoCompletionDto.SearchAutoCompletionListDto.builder().autoCompletionList(autoCompletionList).build();

        return searchAutoCompletionListDto;
    }
}
