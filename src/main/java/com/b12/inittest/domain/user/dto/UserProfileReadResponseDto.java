package com.b12.inittest.domain.user.dto;

import com.b12.inittest.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileReadResponseDto {
    private String userName;

    public static UserProfileReadResponseDto of(User user) {
        return UserProfileReadResponseDto.builder()
                .userName(user.getUserName())
                .build();
    }
}
