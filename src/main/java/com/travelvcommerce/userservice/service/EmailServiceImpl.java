package com.travelvcommerce.userservice.service;

import com.travelvcommerce.userservice.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Transactional
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final StringRedisTemplate redisTemplate;

    private static final String FROM_ADDRESS = "myapplication0513@gmail.com";

    @Override
    public void sendMail(String email, String title, String message) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setFrom(FROM_ADDRESS);
        mailMessage.setSubject(title);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }

    //6자리 무작위 숫자 생성
    @Override
    public String generateVerificationCode() {
        SecureRandom secureRandom = new SecureRandom(); //threadsafe
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            stringBuilder.append(secureRandom.nextInt(10));
        }
        return stringBuilder.toString();
    }

    //5분 후 만료, email을 key로 하여 code를 value로 저장
    @Override
    public void saveVerificationCode(String email, String code) {
        redisTemplate.opsForValue().set(email, code);
        redisTemplate.expire(email, 60 * 5, java.util.concurrent.TimeUnit.SECONDS);
    }

    @Override
    public boolean checkVerificationCode(String email, String code) {
        String verificationCode = redisTemplate.opsForValue().get(email);
        boolean result = code.equals(verificationCode);

        // 인증 코드가 일치하지 않을 경우, 해당 이메일에 대한 Redis 항목 삭제
        if (!result) {
            redisTemplate.delete(email);
        }
        return result;
    }

    @Override
    public void extendTTL(String email, int seconds) {
        redisTemplate.expire(email, seconds, java.util.concurrent.TimeUnit.SECONDS);
    }

    @Override
    public void deleteVerificationCode(String email) {
        redisTemplate.delete(email);
    }

    @Override
    public boolean isEmptyVerificationCode(String email) {
        String code = redisTemplate.opsForValue().get(email);
        return code == null || code.isEmpty();
    }
}
