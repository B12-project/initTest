package com.b12.inittest.common.exception.customException;

import com.b12.inittest.common.exception.errorCode.ErrorCode;

public class UserException extends GlobalCustomException {
    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
