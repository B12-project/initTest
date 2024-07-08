package com.b12.inittest.domain.user.entity;

import com.b12.inittest.domain.common.entity.TimeStamped;
import com.b12.inittest.domain.user.dto.SignUpRequestDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_user")
public class User extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRole userRole;

    private String refreshToken;

    @Builder
    public User(String email, String password, String userName, UserRole userRole) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.userRole = userRole;
    }

    public static User saveUserData(SignUpRequestDto requestDto, String password, UserRole userRole) {
        return User.builder()
                .email(requestDto.getEmail())
                .password(password)
                .userName(requestDto.getUserName())
                .userRole(userRole)
                .build();
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
