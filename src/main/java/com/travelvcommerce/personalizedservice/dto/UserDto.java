package com.travelvcommerce.personalizedservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private Long id;
    private String userId;
    private String nickname;
    private LocalDate birthdate;

    @Data
    public static class UserResponseDto{
        private Long id;
        private String userId;
        private String nickname;
        private LocalDate birthdate;
    }
}
