package com.travelvcommerce.personalizedservice.service;

import com.travelvcommerce.personalizedservice.dto.TagDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface TagService {
    List<String> getRecommendedTagIdList(String userId);

    Map<String, String> initSubscribedTagList(String userId, TagDto.InitTagListRequestDto initTagListRequestDto);
    Map<String, String> subscribeTag(String userId, TagDto.SubscribeTagRequestDto subscribeTagRequestDto);
    Map<String, String> unsubscribeTag(String userId, String tagId);
    Map<String, String> viewTag(String userId, List<String> tagIdList);
    List<String> getSubscribedTagList(String userId);
}
