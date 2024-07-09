package com.b12.inittest.global.exception.customException;

import com.b12.inittest.global.exception.errorCode.ErrorCode;

public class TokenException extends GlobalCustomException{
    public TokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
