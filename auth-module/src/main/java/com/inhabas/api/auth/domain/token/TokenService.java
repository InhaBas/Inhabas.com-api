package com.inhabas.api.auth.domain.token;

import com.inhabas.api.auth.domain.token.jwtUtils.InvalidJwtTokenException;
import com.inhabas.api.auth.domain.token.jwtUtils.refreshToken.RefreshToken;
import com.inhabas.api.auth.domain.token.jwtUtils.refreshToken.RefreshTokenNotFoundException;
import com.inhabas.api.auth.domain.token.jwtUtils.refreshToken.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public void saveRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }


    @Transactional(readOnly = true)
    public TokenDto reissueAccessToken(HttpServletRequest request) throws RefreshTokenNotFoundException, InvalidJwtTokenException {
        return TokenDto.builder().build();
//        String resolvedRefreshTokenString = tokenProvider.resolveToken(request);
//
//        if (Objects.isNull(resolvedRefreshTokenString)) {
//            throw new NoTokenInRequestHeaderException();
//        }
//
//        if (doesNotExist(resolvedRefreshTokenString)) {
//            throw new RefreshTokenNotFoundException();
//        }
//
//        return tokenProvider.reissueAccessTokenUsing(resolvedRefreshTokenString);
    }

    private boolean doesNotExist(String resolvedRefreshTokenString) {
        return !refreshTokenRepository.existsByRefreshToken(resolvedRefreshTokenString);
    }
}
