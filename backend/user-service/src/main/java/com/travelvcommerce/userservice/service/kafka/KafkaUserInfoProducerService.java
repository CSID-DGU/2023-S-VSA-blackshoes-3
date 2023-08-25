package com.travelvcommerce.userservice.service.kafka;

import com.travelvcommerce.userservice.dto.UserDto;

public interface KafkaUserInfoProducerService {
    void createUser(UserDto.UserInfoDto userInfoDto);
    void updateUser(UserDto.UserInfoDto userInfoDto);
    void deleteUser(String userId);

}
