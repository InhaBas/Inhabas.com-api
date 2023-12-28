package com.inhabas.api.domain.signUpSchedule.domain.exception;

import com.inhabas.api.auth.domain.error.ErrorCode;
import com.inhabas.api.auth.domain.error.businessException.BusinessException;

public class SignUpNotAvailableException extends BusinessException {

    public SignUpNotAvailableException() {
        super(ErrorCode.SIGNUP_NOT_AVAILABLE);
    }

}
