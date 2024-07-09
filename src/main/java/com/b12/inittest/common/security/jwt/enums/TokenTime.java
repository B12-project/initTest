package com.b12.inittest.common.security.jwt.enums;

import com.b12.inittest.domain.user.entity.UserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenTime {
    USER_TOKEN_TIME(UserRole.USER, 30 * 60 * 1000L, 7 * 24 * 60 * 60 * 1000L),
    ADMIN_TOKEN_TIME(UserRole.ADMIN, 30 * 60 * 1000L, 60 * 60 * 1000L);

    private final UserRole userRole;
    private final Long accessTokenTime;
    private final Long refreshTokenTime;

    public static TokenTime getValidTokenTime(UserRole userRole) {
        for (TokenTime tokenTime : TokenTime.values()) {
            if (tokenTime.userRole.equals(userRole)) {
                return tokenTime;
            }
        }
        return null;
    }
}
