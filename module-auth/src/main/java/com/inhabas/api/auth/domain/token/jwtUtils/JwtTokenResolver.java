package com.inhabas.api.auth.domain.token.jwtUtils;

import com.inhabas.api.auth.domain.token.TokenResolver;
import javax.servlet.http.HttpServletRequest;

import com.inhabas.api.auth.domain.token.exception.MissingTokenException;
import org.springframework.util.StringUtils;

public class JwtTokenResolver implements TokenResolver {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    public String resolveTokenOrNull(HttpServletRequest request) throws MissingTokenException {

        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer "))
            return bearerToken.substring(7);
        else
            throw new MissingTokenException();
    }
}
