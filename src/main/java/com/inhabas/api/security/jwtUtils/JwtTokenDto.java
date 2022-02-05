package com.inhabas.api.security.jwtUtils;

import lombok.Builder;
import lombok.Getter;

@Getter
public class JwtTokenDto {

    private final String grantType;
    private final String accessToken;
    private final String refreshToken;
    private final Long accessTokenExpireDate;

    @Builder
    public JwtTokenDto(String grantType, String accessToken, String refreshToken, Long accessTokenExpireDate) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpireDate = accessTokenExpireDate;
    }
}
