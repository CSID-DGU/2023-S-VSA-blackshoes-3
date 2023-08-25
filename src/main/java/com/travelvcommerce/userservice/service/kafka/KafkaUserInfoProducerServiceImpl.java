package com.travelvcommerce.userservice.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelvcommerce.userservice.dto.SellerDto;
import com.travelvcommerce.userservice.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaUserInfoProducerServiceImpl implements KafkaUserInfoProducerService{
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    KafkaProducer kafkaProducer;


    @Override
    public void createUser(UserDto.UserInfoDto userInfoDto) {
        String topic = "user-create";

        String userJsonString;
        try {
            userJsonString = objectMapper.writeValueAsString(userInfoDto);
        } catch (Exception e) {
            log.error("Mapping dto to json string error", e);
            throw new RuntimeException("Error publishing created user");
        }

        kafkaProducer.send(topic, userJsonString);
    }

    @Override
    public void updateUser(UserDto.UserInfoDto userInfoDto) {
        String topic = "user-update";

        String userJsonString;
        try {
            userJsonString = objectMapper.writeValueAsString(userInfoDto);
        } catch (Exception e) {
            log.error("Mapping dto to json string error", e);
            throw new RuntimeException("Error publishing updated user");
        }

        kafkaProducer.send(topic, userJsonString);
    }

    @Override
    public void deleteUser(String userId) {
        String topic = "user-delete";

        kafkaProducer.send(topic, userId);
    }
}
