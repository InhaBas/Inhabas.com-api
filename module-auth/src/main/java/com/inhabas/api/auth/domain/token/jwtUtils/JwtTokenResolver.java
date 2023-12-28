package com.inhabas.api.auth.domain.token.jwtUtils;

import com.inhabas.api.auth.domain.token.TokenResolver;
import javax.servlet.http.HttpServletRequest;

import com.inhabas.api.auth.domain.token.exception.TokenMissingException;
import org.springframework.util.StringUtils;

public class JwtTokenResolver implements TokenResolver {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_TYPE = "Bearer ";

    @Override
    public String resolveAccessTokenOrNull(HttpServletRequest request) throws TokenMissingException {

        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AUTHORIZATION_TYPE))
            return bearerToken.substring(7);
        else
            throw new TokenMissingException();
    }
}
