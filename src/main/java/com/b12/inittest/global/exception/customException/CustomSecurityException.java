package com.b12.inittest.global.exception.customException;

import com.b12.inittest.global.exception.errorCode.ErrorCode;

public class CustomSecurityException extends GlobalCustomException {
    public CustomSecurityException(ErrorCode errorCode) {
        super(errorCode);
    }
}
