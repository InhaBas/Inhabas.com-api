package com.inhabas.api.domain.signUp.exception;

import com.inhabas.api.auth.domain.error.ErrorCode;
import com.inhabas.api.auth.domain.error.businessException.BusinessException;

public class NotWriteProfileException extends BusinessException {

    public NotWriteProfileException() {
        super(ErrorCode.NOT_WRITE_PROFILE);
    }

}