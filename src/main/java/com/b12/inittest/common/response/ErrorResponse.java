package com.b12.inittest.common.response;

import com.b12.inittest.common.exception.errorCode.ErrorCode;
import lombok.Getter;

@Getter
public class ErrorResponse<T> extends BasicResponse {
    public ErrorResponse(Boolean isSuccess, Integer statusCode, String message, Object data) {
        super(isSuccess, statusCode, message, data);
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(false, errorCode.getHttpStatusCode(), errorCode.getErrorDescription(), null);
    }
}
