package com.inhabas.api.domain.signUp.domain.exception;

import com.inhabas.api.auth.domain.error.ErrorCode;
import com.inhabas.api.auth.domain.error.businessException.BusinessException;

public class NotWriteAnswersException extends BusinessException {

    public NotWriteAnswersException() {
        super(ErrorCode.NOT_WRITE_ANSWERS);
    }

}