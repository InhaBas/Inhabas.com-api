package com.inhabas.api.auth.domain.token;

import javax.servlet.http.HttpServletRequest;

public interface TokenReIssuer {

    TokenDto reissueAccessToken(HttpServletRequest request) throws InvalidTokenException;

}
