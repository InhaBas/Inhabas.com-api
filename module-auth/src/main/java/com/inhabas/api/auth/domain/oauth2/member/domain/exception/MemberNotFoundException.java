package com.inhabas.api.auth.domain.oauth2.member.domain.exception;

import com.inhabas.api.auth.domain.error.ErrorCode;
import com.inhabas.api.auth.domain.error.businessException.BusinessException;

public class MemberNotFoundException extends BusinessException {

  public MemberNotFoundException() {
    super(ErrorCode.MEMBER_NOT_FOUND);
  }
}
