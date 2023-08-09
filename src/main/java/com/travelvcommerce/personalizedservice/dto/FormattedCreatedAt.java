package com.travelvcommerce.personalizedservice.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface FormattedCreatedAt {
    LocalDateTime getCreatedAt();

    default String getFormattedCreatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        return getCreatedAt().format(formatter);
    }
}