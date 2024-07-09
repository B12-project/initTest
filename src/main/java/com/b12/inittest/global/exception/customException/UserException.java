package com.b12.inittest.global.exception.customException;

import com.b12.inittest.global.exception.errorCode.ErrorCode;

public class UserException extends GlobalCustomException {
    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
