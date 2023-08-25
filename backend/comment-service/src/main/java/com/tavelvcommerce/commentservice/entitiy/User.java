package com.tavelvcommerce.commentservice.entitiy;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Table(name = "users", uniqueConstraints = @UniqueConstraint(name = "user_name", columnNames = {"user_id"}))
@Entity
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "nickname")
    private String nickname;

    @OneToMany(mappedBy = "user")
    private List<Comment> commentList;

    @Builder
    public User(String userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}
