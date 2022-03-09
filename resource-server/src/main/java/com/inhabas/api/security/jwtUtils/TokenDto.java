package com.inhabas.api.security.jwtUtils;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenDto {

    private final String grantType;
    private final String accessToken;
    private final String refreshToken;
    private final Long expiresIn;

    @Builder
    public TokenDto(String grantType, String accessToken, String refreshToken, Long accessTokenExpireDate) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = accessTokenExpireDate;
    }
}
