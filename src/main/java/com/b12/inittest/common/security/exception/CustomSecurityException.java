package com.b12.inittest.common.security.exception;

import com.b12.inittest.common.exception.customException.GlobalCustomException;
import com.b12.inittest.common.exception.errorCode.ErrorCode;

public class CustomSecurityException extends GlobalCustomException {
    public CustomSecurityException(ErrorCode errorCode) {
        super(errorCode);
    }
}
