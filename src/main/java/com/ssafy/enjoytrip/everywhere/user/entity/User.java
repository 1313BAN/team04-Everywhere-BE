package com.ssafy.enjoytrip.everywhere.user.entity;

import com.ssafy.enjoytrip.everywhere.common.constants.Role;
import com.ssafy.enjoytrip.everywhere.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(length = 200)
    private String refreshToken;

    @Builder
    public User(String userId, String password, String nickname, Role role) {
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

    public void updateRefreshToken(String token) {
        this.refreshToken = token;
    }
}