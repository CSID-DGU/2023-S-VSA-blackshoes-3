package com.tavelvcommerce.commentservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tavelvcommerce.commentservice.dto.UserInfoDto;
import com.tavelvcommerce.commentservice.entitiy.Comment;
import com.tavelvcommerce.commentservice.entitiy.User;
import com.tavelvcommerce.commentservice.repository.CommentRepository;
import com.tavelvcommerce.commentservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaUserInfoConsumerServiceImpl implements KafkaUserInfoConsumerService {
    private final ObjectMapper objectMapper;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    @KafkaListener(topics = "user-create")
    @Transactional
    public void createUserNickName(String payLoad, Acknowledgment acknowledgment) {
        log.info("received payload = '{}'", payLoad);

        UserInfoDto userInfoDto;

        try {
            userInfoDto = objectMapper.readValue(payLoad, UserInfoDto.class);
        } catch (Exception e) {
                log.error("Error converting to user info dto", e);
                return;
        }

        String userId = userInfoDto.getUserId();
        String username = userInfoDto.getNickname();

        User user = User.builder()
                .userId(userId)
                .nickname(username)
                .build();

        try {
            userRepository.save(user);
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("Error saving user nickname", e);
            return;
        }
    }

    @Override
    @KafkaListener(topics = "user-update")
    @Transactional
    public void updateUserNickname(String payload, Acknowledgment acknowledgment) {
        log.info("received payload='{}'", payload);

        UserInfoDto userInfoDto;
        try {
            userInfoDto = objectMapper.readValue(payload, UserInfoDto.class);
        } catch (Exception e) {
            log.error("Error converting payload to user info dto", e);
            return;
        }

        String userId = userInfoDto.getUserId();
        String nickname = userInfoDto.getNickname();

        try {
            User user = userRepository.findByUserId(userId).orElseThrow(() -> new NoSuchElementException("User not found"));
            user.updateNickname(nickname);
            acknowledgment.acknowledge();
        } catch (NoSuchElementException e) {
            log.error("User not found", e);
            return;
        } catch (Exception e) {
            log.error("Error updating user nickname", e);
            return;
        }
    }

    @Override
    @KafkaListener(topics = "user-delete")
    @Transactional
    public void deleteUserComment(String payload, Acknowledgment acknowledgment) {
        log.info("received payload='{}'", payload);

        String userId = payload;

        try {
            User user = userRepository.findByUserId(userId).orElseThrow(() -> new NoSuchElementException("User not found"));
            userRepository.delete(user);
            acknowledgment.acknowledge();
        } catch (NoSuchElementException e) {
            log.error("User not found", e);
            return;
        } catch (Exception e) {
            log.error("Error deleting user comment", e);
            return;
        }
    }
}
