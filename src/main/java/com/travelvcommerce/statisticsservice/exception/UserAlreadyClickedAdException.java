package com.travelvcommerce.statisticsservice.exception;

public class UserAlreadyClickedAdException extends RuntimeException {
    public UserAlreadyClickedAdException(String message) {
        super(message);
    }

    UserAlreadyClickedAdException() {
        super();
    }
}
