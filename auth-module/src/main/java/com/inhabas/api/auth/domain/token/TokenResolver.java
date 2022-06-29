package com.inhabas.api.auth.domain.token;

import javax.servlet.http.HttpServletRequest;

public interface TokenResolver {

    /**
     *
     * @param request HttpServletRequest
     * @return a resolved token from request header, otherwise null
     */
    String resolveTokenOrNull(HttpServletRequest request);

}
