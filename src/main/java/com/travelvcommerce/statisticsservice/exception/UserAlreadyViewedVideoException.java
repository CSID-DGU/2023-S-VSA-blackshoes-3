package com.travelvcommerce.statisticsservice.exception;

import org.springframework.stereotype.Component;

@Component
public class UserAlreadyViewedVideoException extends RuntimeException {
    public UserAlreadyViewedVideoException(String message) {
        super(message);
    }

    UserAlreadyViewedVideoException() {
        super();
    }
}
