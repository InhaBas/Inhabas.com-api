package com.inhabas.api.auth.domain.token;

import com.inhabas.api.auth.domain.token.exception.InvalidTokenException;

import javax.servlet.http.HttpServletRequest;

public interface TokenReIssuer {

    TokenDto reissueAccessToken(String refreshToken) throws InvalidTokenException;

}
