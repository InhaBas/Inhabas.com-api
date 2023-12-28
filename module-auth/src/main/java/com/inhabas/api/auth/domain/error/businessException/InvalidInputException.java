package com.inhabas.api.auth.domain.error.businessException;

import com.inhabas.api.auth.domain.error.ErrorCode;

public class InvalidInputException extends BusinessException {

    public InvalidInputException() {
        super(ErrorCode.INVALID_INPUT_VALUE);
    }

}

