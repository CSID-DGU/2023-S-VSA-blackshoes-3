package com.travelvcommerce.userservice.service;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public interface EmailService {
    void sendMail(String email, String title, String message);
    String generateVerificationCode();

    void saveVerificationCode(String email, String code);

    boolean checkVerificationCode(String email, String code);

    void deleteVerificationCode(String email);

    void deleteCompletionCode(String email);

    void saveCompletionCode(String email);

    boolean isExistVerificationCode(String email);

    boolean isExistCompletionCode(String email);
}
