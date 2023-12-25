package com.inhabas.api.domain.menu;

import com.inhabas.api.auth.domain.error.ErrorCode;
import com.inhabas.api.auth.domain.error.businessException.BusinessException;

public class MenuNotExistException extends BusinessException {

    public MenuNotExistException() {
        super(ErrorCode.INVALID_MENU);
    }

}
