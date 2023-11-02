package com.inhabas.api.auth.domain.token.jwtUtils;

import com.inhabas.api.auth.domain.token.exception.InvalidTokenException;
import com.inhabas.api.auth.domain.token.TokenDto;
import com.inhabas.api.auth.domain.token.TokenProvider;
import com.inhabas.api.auth.domain.token.TokenReIssuer;
import com.inhabas.api.auth.domain.token.TokenResolver;
import com.inhabas.api.auth.domain.token.jwtUtils.refreshToken.RefreshTokenNotFoundException;
import com.inhabas.api.auth.domain.token.jwtUtils.refreshToken.RefreshTokenRepository;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtTokenReIssuer implements TokenReIssuer {

    private final TokenProvider tokenProvider;
    private final TokenResolver tokenResolver;
    private final RefreshTokenRepository refreshTokenRepository;


    @Override
    public TokenDto reissueAccessToken(HttpServletRequest request) throws InvalidTokenException {

        String refreshToken = tokenResolver.resolveTokenOrNull(request);

        if (!tokenProvider.validate(refreshToken) ) {
            throw new InvalidTokenException();
        }

        if (!refreshTokenRepository.existsByRefreshToken(refreshToken)) {
            throw new RefreshTokenNotFoundException();
        }

        return tokenProvider.reissueAccessTokenUsing(refreshToken);
    }
}
