package com.travelvcommerce.userservice.dto.request;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDto {
    private String email;
    private String password;
    private String name;
    private LocalDate birthdate;
    private String role;
    private String provider;
    private String providerId;
}