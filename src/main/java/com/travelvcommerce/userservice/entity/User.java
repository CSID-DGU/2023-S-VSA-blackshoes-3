package com.travelvcommerce.userservice.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "birthdate")
    private LocalDate birthdate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column(name = "provider")
    private String provider;

    @Column(name = "provider_id")
    private String providerId;

//    @PreUpdate
//    public void updatedAt() {
//        this.updatedAt = LocalDateTime.now();
//    }

//    @PrePersist
//    public void createdDate() {
//        this.createdAt = LocalDateTime.now();
//    }

    @Builder
    public User(String userId, String email, String password, String nickname, LocalDate birthdate, LocalDateTime createdAt, LocalDateTime updatedAt, Role role, String provider, String providerId) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.birthdate = birthdate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }

    public void update(String nickname) {
        this.nickname = nickname;
        this.updatedAt = LocalDateTime.now();
    }
}