package com.travelvcommerce.contentslaveservice.service;

import com.travelvcommerce.contentslaveservice.vo.UserPersonalizedData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

// 테스트용 임시 구현체 -> 차후 서비스간 연동으로 실제 정보 조회
@Service
@Slf4j
public class UserPersonalizedServiceTmp implements UserPersonalizedService {
    @Override
    public UserPersonalizedData getPersonalizedHistory(String userId) {
        UserPersonalizedData userPersonalizedData = new UserPersonalizedData();
        userPersonalizedData.setIdType("videoId");
        userPersonalizedData.setIdList(List.of("v43039a5-2199-11ee-9ef2-0a0027000003",
                "v43039aa-2199-11ee-9ef2-0a0027000003",
                "v43039ad-2199-11ee-9ef2-0a0027000003"));
        return userPersonalizedData;
    }

    @Override
    public UserPersonalizedData getPersonalizedTags(String userId) {
        UserPersonalizedData userPersonalizedData = new UserPersonalizedData();
        userPersonalizedData.setIdType("videoTags.tagId");
        userPersonalizedData.setIdList(List.of("e43032aa-2199-11ee-9ef2-0a0027000003",
                "e4303860-2199-11ee-9ef2-0a0027000003",
                "e430381f-2199-11ee-9ef2-0a0027000003"));
        return userPersonalizedData;
    }

    @Override
    public UserPersonalizedData getPersonalizedLikes(String userId) {
        UserPersonalizedData userPersonalizedData = new UserPersonalizedData();
        userPersonalizedData.setIdType("videoId");
        userPersonalizedData.setIdList(List.of("v43039a5-2199-11ee-9ef2-0a0027000003",
                "v43039aa-2199-11ee-9ef2-0a0027000003",
                "v43039ad-2199-11ee-9ef2-0a0027000003",
                "v43039b8-2199-11ee-9ef2-0a0027000003"));
        return userPersonalizedData;
    }
}
