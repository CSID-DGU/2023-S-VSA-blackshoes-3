package com.tavelvcommerce.commentservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoDto {
    String userId;
    String nickname;
}
