package com.b12.inittest.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SecurityErrorCode implements ErrorCode{
    JWT_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "토큰이 존재하지 않습니다."),
    INVALID_JWT_TOKEN(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 토큰입니다."),
    EXPIRED_JWT_TOKEN(HttpStatus.BAD_REQUEST.value(), "만료된 토큰입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.BAD_REQUEST.value(), "지원되지 않는 토큰입니다."),
    REFRESH_NOT_FOUND(HttpStatus.BAD_REQUEST.value(),
            "리프레쉬 토큰이 존재하지 않습니다."),
    LOGIN_FAILED(HttpStatus.BAD_REQUEST.value(), "로그인에 실패했습니다."),
    EMAIL_PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST.value(), "아이디와 비밀번호가 일치하지 않습니다."),
    USER_DEACTIVATE(HttpStatus.FORBIDDEN.value(), "탈퇴한 유저입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "일치하는 유저를 찾을 수 없습니다.");


    private final Integer httpStatusCode;
    private final String errorDescription;
}
