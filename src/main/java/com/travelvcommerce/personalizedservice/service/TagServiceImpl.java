package com.travelvcommerce.personalizedservice.service;

import com.travelvcommerce.personalizedservice.dto.TagDto;
import com.travelvcommerce.personalizedservice.entity.SubscribedTag;
import com.travelvcommerce.personalizedservice.entity.ViewTag;
import com.travelvcommerce.personalizedservice.repository.SubscribedTagRepository;
import com.travelvcommerce.personalizedservice.repository.ViewTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
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
    public List<String> getRecommendedTagIdList(String userId) {
        try{
        Optional<ViewTag> viewTag = viewTagRepository.findByUserId(userId);
        List<Set<String>> userTagIdSetList = viewTag.get().getTagIdSetList();

        // 1. 특정 사용자에 대한 모든 태그의 TF 값을 계산
        Map<String, Double> tfValues = calculateTF(userTagIdSetList);

        List<ViewTag> allUserViewTags = viewTagRepository.findAll();
        List<List<Set<String>>> allUsersTagIdSetList = allUserViewTags.stream()
                .map(ViewTag::getTagIdSetList)
                .collect(Collectors.toList());

        // 2. 모든 태그의 IDF 값을 계산
        Map<String, Double> idfValues = calculateIDFForAllUsers(allUsersTagIdSetList);

        // 3. TF-IDF 값을 계산
        Map<String, Double> tfidfValues = new HashMap<>();
        for (String tag : tfValues.keySet()) {
            double tfidf = tfValues.get(tag) * idfValues.getOrDefault(tag, 0.0);
            tfidfValues.put(tag, tfidf);
        }

        // 4. TF-IDF 값이 가장 큰 상위 5개의 태그를 추천
        List<String> recommendedTags = tfidfValues.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(5)
                .collect(Collectors.toList());

        return recommendedTags;
        } catch (Exception e){
            log.error("Error in getRecommendedTagIdList", e);
            return new ArrayList<>();
        }
    }
    private Map<String, Double> calculateTF(List<Set<String>> userTagIdSetList) {
        try {

            Map<String, Integer> tagFrequencyMap = new HashMap<>();
            Set<String> allUniqueTags = new HashSet<>();

            for (Set<String> tagIdSet : userTagIdSetList) {
                allUniqueTags.addAll(tagIdSet);
                tagIdSet.forEach(tag -> tagFrequencyMap.put(tag, tagFrequencyMap.getOrDefault(tag, 0) + 1));
            }

            Map<String, Double> tfMap = new HashMap<>();
            for (String tag : allUniqueTags) {
                double tfValue = (double) tagFrequencyMap.get(tag) / allUniqueTags.size();
                tfMap.put(tag, tfValue);
            }

            return tfMap;

        } catch (Exception e) {
            log.error("Error in calculateTF", e);
            return new HashMap<>();
        }
    }
    private Map<String, Double> calculateIDFForAllUsers(List<List<Set<String>>> allUsersTagIdSetList) {
        try {
            Map<String, Integer> tagDocumentFrequency = new HashMap<>();

            // "전체 문서수" 계산 (모든 사용자들의 모든 tagIdSet의 합)
            int totalDocuments = 0;
            for (List<Set<String>> userTagIdSetList : allUsersTagIdSetList) {
                totalDocuments += userTagIdSetList.size();
            }

            for (List<Set<String>> userTagIdSetList : allUsersTagIdSetList) {
                Set<String> allTagsInThisUser = new HashSet<>();
                for (Set<String> tagIdSet : userTagIdSetList) {
                    allTagsInThisUser.addAll(tagIdSet);
                }

                for (String tag : allTagsInThisUser) {
                    tagDocumentFrequency.put(tag, tagDocumentFrequency.getOrDefault(tag, 0) + 1);
                }
            }

            // Compute IDF for each tag
            Map<String, Double> idfMap = new HashMap<>();
            for (String tag : tagDocumentFrequency.keySet()) {
                double idfValue = Math.log((double) totalDocuments / tagDocumentFrequency.get(tag));
                idfMap.put(tag, idfValue);
            }

            return idfMap;
        }catch(Exception e){
            log.error("Error in calculateIDFForAllUsers", e);
            return new HashMap<>();
        }
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
    public Map<String, String> viewTag(String userId, List<String> tagIdList) {
        Map<String, String> viewTagResponse = new HashMap<>();

        //tagIdList to set
        Set<String> tagSetList = tagIdList.stream().collect(Collectors.toSet());

        log.info("tagSetList : " + tagSetList);

        ViewTag viewTag;

        if(viewTagRepository.existsByUserId(userId)){
            viewTag = viewTagRepository.findByUserId(userId).get();

            //tagSetList에 tagSet 추가
            viewTag.appendTagIdSet(tagSetList);
            viewTagRepository.save(viewTag);
            viewTagResponse.put("updatedAt", viewTag.getUpdatedAt());
        } else {
            viewTag = ViewTag.builder().userId(userId).build();
            viewTag.initializeTagIdSet();
            viewTag.appendTagIdSet(tagSetList);
            viewTagRepository.save(viewTag);
            viewTagResponse.put("createdAt", viewTag.getCreatedAt());
        }
        viewTagResponse.put("userId", userId);

        return viewTagResponse;
    }

}
