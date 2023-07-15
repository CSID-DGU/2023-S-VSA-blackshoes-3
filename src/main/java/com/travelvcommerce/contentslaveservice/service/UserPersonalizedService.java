package com.travelvcommerce.contentslaveservice.service;

import com.travelvcommerce.contentslaveservice.vo.UserPersonalizedData;

public interface UserPersonalizedService {
    UserPersonalizedData getPersonalizedHistory(String userId);
    UserPersonalizedData getPersonalizedTags(String userId);
    UserPersonalizedData getPersonalizedLikes(String userId);
}
