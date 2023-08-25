package com.travelvcommerce.statisticsservice.exception;

public class UserAlreadyLikedVideoException extends RuntimeException {
    public UserAlreadyLikedVideoException(String message) {
        super(message);
    }

    UserAlreadyLikedVideoException() {
        super();
    }
}
