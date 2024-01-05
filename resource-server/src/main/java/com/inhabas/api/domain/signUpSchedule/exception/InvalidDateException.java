package com.inhabas.api.domain.signUpSchedule.exception;

import com.inhabas.api.auth.domain.error.ErrorCode;
import com.inhabas.api.auth.domain.error.businessException.BusinessException;

public class InvalidDateException extends BusinessException {

    public InvalidDateException(ErrorCode errorCode) {
        super(errorCode);
    }

}
