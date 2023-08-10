package com.travelvcommerce.personalizedservice.service;

import com.travelvcommerce.personalizedservice.dto.TagDto;
import com.travelvcommerce.personalizedservice.entity.SubscribedTag;
import com.travelvcommerce.personalizedservice.entity.ViewTag;
import com.travelvcommerce.personalizedservice.repository.SubscribedTagRepository;
import com.travelvcommerce.personalizedservice.repository.ViewTagRepository;
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
    private final ViewTagRepository viewTagRepository;
    @Override
    public List<String> getSubscribedTagList(String userId) {
        if(!subscribedTagRepository.existsByUserId(userId)){
            throw new CustomBadRequestException("Invalid user id");
        }

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
                .createdAt(LocalDateTime.now())
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
        subscribeTagResponse.put("createdAt", subscribeTagResponseDto.getFormattedCreatedAt());

        return subscribeTagResponse;
    }

    @Override
    public Map<String, String> unsubscribeTag(String userId, TagDto.UnsubscribeTagRequestDto unsubscribeTagRequestDto) {

        if(!subscribedTagRepository.existsByUserIdAndTagId(userId, unsubscribeTagRequestDto.getTagId())){
            throw new CustomBadRequestException("Invalid tag id or user id");
        }

        subscribedTagRepository.deleteByUserIdAndTagId(userId, unsubscribeTagRequestDto.getTagId());

        Map<String, String> unsubscribeTagResponse = new HashMap<>();
        unsubscribeTagResponse.put("userId", userId);

        return unsubscribeTagResponse;
    }

    @Override
    public Map<String, String> viewTag(String userId, TagDto.ViewTagRequestDto viewTagRequestDto) {
        TagDto.ViewTagResponseDto viewTagResponseDto = new TagDto.ViewTagResponseDto();

        if(viewTagRepository.existsByUserIdAndTagId(userId, viewTagRequestDto.getTagId())){
            ViewTag viewTag = viewTagRepository.findByUserIdAndTagId(userId, viewTagRequestDto.getTagId());
            viewTag.setUpdatedAt(LocalDateTime.now());
            viewTag.setTagViewCount(viewTag.getTagViewCount()+1);
            viewTagRepository.save(viewTag);

            viewTagResponseDto.setTagViewCount(viewTag.getTagViewCount());
            viewTagResponseDto.setCreatedAt(viewTag.getCreatedAt());
        }else{
            ViewTag viewTag = ViewTag.builder()
                    .userId(userId)
                    .tagId(viewTagRequestDto.getTagId())
                    .createdAt(LocalDateTime.now())
                    .tagViewCount(1L)
                    .build();
            viewTagRepository.save(viewTag);

            viewTagResponseDto.setTagViewCount(viewTag.getTagViewCount());
            viewTagResponseDto.setCreatedAt(LocalDateTime.now());
        }

        viewTagResponseDto.setUserId(userId);
        viewTagResponseDto.setUpdatedAt(LocalDateTime.now());

        Map<String, String> viewTagResponse = new HashMap<>();
        viewTagResponse.put("userId", userId);
        viewTagResponse.put("createdAt", viewTagResponseDto.getFormattedCreatedAt());
        viewTagResponse.put("updatedAt", viewTagResponseDto.getFormattedUpdatedAt());
        viewTagResponse.put("tagViewCount", viewTagResponseDto.getTagViewCount().toString());
        return viewTagResponse;
    }
}
