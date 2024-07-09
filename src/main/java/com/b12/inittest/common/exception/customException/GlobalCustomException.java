package com.b12.inittest.common.exception.customException;

import com.b12.inittest.common.exception.errorCode.ErrorCode;
import lombok.Getter;

@Getter
public class GlobalCustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public GlobalCustomException(ErrorCode errorCode) {
        super(errorCode.getErrorDescription());
        this.errorCode = errorCode;
    }
}
