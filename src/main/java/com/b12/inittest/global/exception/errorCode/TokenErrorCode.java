package com.b12.inittest.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TokenErrorCode implements ErrorCode {
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST.value(), "리프레쉬 토큰이 유효하지 않습니다. 다시 로그인해주세요.");

    private final Integer httpStatusCode;
    private final String errorDescription;
}
