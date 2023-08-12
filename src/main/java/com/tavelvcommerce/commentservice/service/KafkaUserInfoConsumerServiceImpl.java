package com.tavelvcommerce.commentservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tavelvcommerce.commentservice.dto.UserInfoDto;
import com.tavelvcommerce.commentservice.entitiy.Comment;
import com.tavelvcommerce.commentservice.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaUserInfoConsumerServiceImpl implements KafkaUserInfoConsumerService {
    private final ObjectMapper objectMapper;
    private final CommentRepository commentRepository;
    @Override
    @KafkaListener(topics = "user-update")
    @Transactional
    public void updateUserNickname(String payload) {
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

        List<Comment> userCommentList = commentRepository.findAllByUserId(userId);
        try {
            for (Comment comment : userCommentList) {
                comment.updateNickname(nickname);
            }
        } catch (Exception e) {
            log.error("Error updating user nickname", e);
            return;
        }
    }

    @Override
    @KafkaListener(topics = "user-delete")
    @Transactional
    public void deleteUserComment(String payload) {
        log.info("received payload='{}'", payload);

        String userId = payload;

        try {
            commentRepository.deleteAllByUserId(userId);
        } catch (Exception e) {
            log.error("Error deleting user comment", e);
            return;
        }
    }
}
