package com.travelvcommerce.statisticsservice.exception;

public class UserDidNotLikedVideoException extends RuntimeException {
    public UserDidNotLikedVideoException(String message) {
        super(message);
    }

    UserDidNotLikedVideoException() {
        super();
    }
}
