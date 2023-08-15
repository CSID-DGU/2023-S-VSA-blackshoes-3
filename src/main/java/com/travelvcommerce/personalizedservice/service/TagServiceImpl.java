package com.travelvcommerce.personalizedservice.service;

import com.travelvcommerce.personalizedservice.dto.TagDto;
import com.travelvcommerce.personalizedservice.entity.SubscribedTag;
import com.travelvcommerce.personalizedservice.entity.ViewTag;
import com.travelvcommerce.personalizedservice.repository.SubscribedTagRepository;
import com.travelvcommerce.personalizedservice.repository.ViewTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TagServiceImpl implements TagService{

    private final SubscribedTagRepository subscribedTagRepository;
    private final ViewTagRepository viewTagRepository;
    @Override
    public List<String> getSubscribedTagList(String userId) {
        if(!subscribedTagRepository.existsByUserId(userId)){
            throw new ResourceNotFoundException("Invalid user id");
        }

        List<SubscribedTag> subscribedTagList = subscribedTagRepository.findByUserId(userId);
        List<String> tagIdList = subscribedTagList.stream().map(SubscribedTag::getTagId).collect(Collectors.toList());

        return tagIdList;
    }

    @Override
    public List<Map<String, Object>> getViewedTagList(String userId) {
        if (!viewTagRepository.existsByUserId(userId)) {
            throw new ResourceNotFoundException("Invalid user id");
        }

        Pageable pageable = PageRequest.of(0, 5, Sort.by("tagViewCount").descending());
        List<ViewTag> viewTagList = viewTagRepository.findByUserId(userId, pageable);

        List<Map<String, Object>> resultList = new ArrayList<>();
        for (ViewTag viewTag : viewTagList) {
            Map<String, Object> tagDetailMap = new HashMap<>();
            tagDetailMap.put("tagId", viewTag.getTagId());
            tagDetailMap.put("viewCount", Math.toIntExact(viewTag.getTagViewCount()));
            tagDetailMap.put("createdAt", viewTag.getCreatedAt());

            resultList.add(tagDetailMap);
        }

        return resultList;
    }


    @Override
    @Transactional
    public Map<String, String> initSubscribedTagList(String userId, TagDto.InitTagListRequestDto initTagListRequestDto){
        for(String tagId : initTagListRequestDto.getTagIdList()){
                SubscribedTag subscribedTag = SubscribedTag.builder()
                .userId(userId)
                .tagId(tagId)
                .createdAt(LocalDateTime.now())
                .build();
            subscribedTagRepository.save(subscribedTag);
        }

        TagDto.InitTagListResponseDto initTagListResponseDto = new TagDto.InitTagListResponseDto();
        initTagListResponseDto.setUserId(userId);
        initTagListResponseDto.setCreatedAt(LocalDateTime.now());

        Map<String, String> initTagListResponse = new HashMap<>();
        initTagListResponse.put("userId", userId);
        initTagListResponse.put("tagIdList", initTagListRequestDto.getTagIdList().toString());
        initTagListResponse.put("createdAt", initTagListResponseDto.getFormattedCreatedAt());

        return initTagListResponse;
    }

    @Override
    @Transactional
    public Map<String, String> subscribeTag(String userId, TagDto.SubscribeTagRequestDto subscribeTagRequestDto) {

        if (!subscribedTagRepository.existsByUserId(userId)){
            throw new CustomBadRequestException("Invalid user id");
        }

        subscribedTagRepository.save(SubscribedTag.builder()
                .userId(userId)
                .tagId(subscribeTagRequestDto.getTagId())
                .createdAt(LocalDateTime.now())
                .build());

        TagDto.SubscribeTagResponseDto subscribeTagResponseDto = new TagDto.SubscribeTagResponseDto();
        subscribeTagResponseDto.setCreatedAt(LocalDateTime.now());

        Map<String, String> subscribeTagResponse = new HashMap<>();
        subscribeTagResponse.put("userId", userId);
        subscribeTagResponse.put("tagId", subscribeTagRequestDto.getTagId());
        subscribeTagResponse.put("createdAt", subscribeTagResponseDto.getFormattedCreatedAt());

        return subscribeTagResponse;
    }

    @Override
    @Transactional
    public Map<String, String> unsubscribeTag(String userId, String tagId) {

        if(!subscribedTagRepository.existsByUserIdAndTagId(userId, tagId)) {
            throw new ResourceNotFoundException("Invalid tag id or user id");
        }

        subscribedTagRepository.deleteByUserIdAndTagId(userId, tagId);

        Map<String, String> unsubscribeTagResponse = new HashMap<>();
        unsubscribeTagResponse.put("userId", userId);

        return unsubscribeTagResponse;
    }

    @Override
    @Transactional
    public Map<String, String> viewTag(String userId, String tagId) {
        Map<String, String> viewTagResponse = new HashMap<>();

        if(viewTagRepository.existsByUserIdAndTagId(userId, tagId)){
            ViewTag viewTag = viewTagRepository.findByUserIdAndTagId(userId, tagId);
            viewTag.increaseViewCount();
            viewTag.setUpdatedAt(LocalDateTime.now());

            viewTagResponse.put("userId", userId);
            viewTagResponse.put("tagId", tagId);
            viewTagResponse.put("createdAt", viewTag.getCreatedAt().toString());
            viewTagResponse.put("updatedAt", viewTag.getUpdatedAt().toString());
        } else {
            ViewTag viewTag = ViewTag.builder()
                    .userId(userId)
                    .tagId(tagId)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .tagViewCount(1L)
                    .build();
            viewTagRepository.save(viewTag);

            viewTagResponse.put("userId", userId);
            viewTagResponse.put("tagId", tagId);
            viewTagResponse.put("createdAt", viewTag.getCreatedAt().toString());
            viewTagResponse.put("updatedAt", viewTag.getUpdatedAt().toString());
        }
        return viewTagResponse;
    }
}
