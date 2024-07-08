package com.b12.inittest.domain.user.dto;

import com.b12.inittest.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpResponseDto {
    private String userName;

    public static SignUpResponseDto of(User user) {
        return SignUpResponseDto.builder()
                .userName(user.getUserName())
                .build();
    }
}
