package com.inhabas.api.security.jwtUtils;

import com.inhabas.api.security.domain.AuthUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.*;

// https://github.com/jwtk/jjwt
@Slf4j
@Component
public class JwtTokenProvider {

    // spring boot 가 실행될 때마다 secretKey 가 매번 달라짐.
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final Long accessTokenValidMilliSecond = 30 * 60 * 1000L; // 0.5 hour
    private static final Long refreshTokenValidMilliSecond = 7 * 24 * 60 * 60 * 1000L; // 7 days
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String ROLE = "role";
    private static final String TEAM = "teams";

    public JwtTokenDto createJwtToken(Integer authUserId, GrantedAuthority role, Set<GrantedAuthority> teams) {

        Claims claims = Jwts.claims().setSubject(String.valueOf(authUserId));
        claims.put(ROLE, role);
        claims.put(TEAM, teams);

        Date now = new Date();

        String accessToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidMilliSecond))
                .signWith(secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidMilliSecond))
                .signWith(secretKey)
                .compact();

        return JwtTokenDto.builder()
                .grantType("bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpireDate(accessTokenValidMilliSecond)
                .build();
    }


}
