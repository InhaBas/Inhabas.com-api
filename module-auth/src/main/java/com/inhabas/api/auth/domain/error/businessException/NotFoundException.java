package com.inhabas.api.auth.domain.error.businessException;

import com.inhabas.api.auth.domain.error.ErrorCode;

public class NotFoundException extends BusinessException {

    public NotFoundException() {
        super(ErrorCode.NOT_FOUND);
    }

}


