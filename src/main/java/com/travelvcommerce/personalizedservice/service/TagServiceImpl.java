package com.travelvcommerce.personalizedservice.service;

import com.travelvcommerce.personalizedservice.dto.TagDto;
import com.travelvcommerce.personalizedservice.entity.SubscribedTag;
import com.travelvcommerce.personalizedservice.repository.SubscribedTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TagServiceImpl implements TagService{

    private final SubscribedTagRepository subscribedTagRepository;

    @Override
    public List<String> getSubscribedTagList(String userId) {
        List<SubscribedTag> subscribedTagList = subscribedTagRepository.findByUserId(userId);
        List<String> tagIdList = subscribedTagList.stream().map(SubscribedTag::getTagId).collect(Collectors.toList());

        return tagIdList;
    }

    @Override
    public Map<String, String> initSubscribedTagList(String userId, TagDto.InitTagListRequestDto initTagListRequestDto){
        for(String tagId : initTagListRequestDto.getTagIdList()){
                SubscribedTag subscribedTag = SubscribedTag.builder()
                .userId(userId)
                .tagId(tagId)
                .build();
            subscribedTagRepository.save(subscribedTag);
        }

        TagDto.InitTagListResponseDto initTagListResponseDto = new TagDto.InitTagListResponseDto();
        initTagListResponseDto.setUserId(userId);
        initTagListResponseDto.setCreatedAt(LocalDateTime.now());

        Map<String, String> initTagListResponse = new HashMap<>();
        initTagListResponse.put("userId", userId);
        initTagListResponse.put("createdAt", initTagListResponseDto.getFormattedCreatedAt());

        return initTagListResponse;
    }

    @Override
    public Map<String, String> subscribeTag(String userId, TagDto.SubscribeTagRequestDto subscribeTagRequestDto) {
        TagDto.SubscribeTagResponseDto subscribeTagResponseDto = new TagDto.SubscribeTagResponseDto();
        subscribeTagResponseDto.setUserId(userId);
        subscribeTagResponseDto.setCreatedAt(LocalDateTime.now());

        Map<String, String> subscribeTagResponse = new HashMap<>();
        subscribeTagResponse.put("userId", userId);
        subscribeTagResponse.put("createdAt", subscribeTagResponseDto.getFormattedCreatedAt());

        return subscribeTagResponse;
    }

    @Override
    public Map<String, String> unsubscribeTag(String userId, TagDto.UnsubscribeTagRequestDto unsubscribeTagRequestDto) {
        subscribedTagRepository.deleteByUserIdAndTagId(userId, unsubscribeTagRequestDto.getTagId());

        TagDto.UnsubscribeTagResponseDto unsubscribeTagResponseDto = new TagDto.UnsubscribeTagResponseDto();
        unsubscribeTagResponseDto.setUserId(userId);
        unsubscribeTagResponseDto.setCreatedAt(LocalDateTime.now());

        Map<String, String> unsubscribeTagResponse = new HashMap<>();
        unsubscribeTagResponse.put("userId", userId);
        unsubscribeTagResponse.put("createdAt", unsubscribeTagResponseDto.getFormattedCreatedAt());

        return unsubscribeTagResponse;
    }

}
