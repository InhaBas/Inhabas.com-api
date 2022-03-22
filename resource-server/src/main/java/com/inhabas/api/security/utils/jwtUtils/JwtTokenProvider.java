package com.inhabas.api.security.utils.jwtUtils;

import com.inhabas.api.security.domain.token.TokenProvider;
import com.inhabas.api.security.domain.token.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.*;

// https://github.com/jwtk/jjwt
@Slf4j
@Component
public class JwtTokenProvider implements TokenProvider {

    // spring boot 가 실행될 때마다 secretKey 가 매번 달라짐.
    private static final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final Long accessTokenValidMilliSecond = 30 * 60 * 1000L; // 0.5 hour
    private static final Long refreshTokenValidMilliSecond = 7 * 24 * 60 * 60 * 1000L; // 7 days
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String ROLE = "role";
    private static final String TEAM = "teams";
    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    public TokenDto createJwtToken(Integer authUserId, String role, Set<String> teams) {
        assert Objects.nonNull(authUserId);
        assert StringUtils.hasText(role);

        Claims claims = Jwts.claims().setSubject(String.valueOf(authUserId));
        claims.put(ROLE, ROLE_PREFIX + role);
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

        return TokenDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpireDate(accessTokenValidMilliSecond)
                .build();
    }

    /* web request 에 대한 인증 정보를 반환함. */
    @Override
    @SuppressWarnings("unchecked")
    public JwtTokenDecodedInfo authenticate(String token) throws JwtException {

        validateToken(token); // if not valid, it will throw jwtException

        Claims claims = this.parseClaims(token);
        Integer userId = Integer.parseInt(claims.getSubject());
        String role = (String) claims.get(ROLE);
        List<String> teams = (ArrayList<String>) claims.get(TEAM);

        Set<GrantedAuthority> totalGrantedAuthorities = new HashSet<>();
        totalGrantedAuthorities.add(new SimpleGrantedAuthority(role));
        if (teams != null) {
            teams.forEach(
                    teamAuthority -> totalGrantedAuthorities.add(new SimpleGrantedAuthority(teamAuthority)));
        }

        return new JwtTokenDecodedInfo(userId, totalGrantedAuthorities);
    }

    /* request 헤더에 담긴 access 토큰을 가져옴. */
    @Override
    public String resolveToken(HttpServletRequest request) {

        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
            return authorizationHeader.substring(7);
        else
            return null;
    }

    /* The key from before is being used to validate the signature of the JWT.
     * If it fails to verify the JWT, a SignatureException (which extends from JwtException) is thrown. */
    private void validateToken(String token) {

        assert StringUtils.hasText(token);

        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
        } catch (JwtException e) {
            log.debug(e.getMessage());
            throw new InvalidJwtTokenException();
        }
    }

    /* 토큰 body 에 넣어둔 사용자 정보를 가져옴
     * validation 검사를 먼저 꼭 해야함! */
    private Claims parseClaims(String token) throws JwtException {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }

    @Override
    public TokenDto reissueAccessTokenUsing(String refreshToken) throws InvalidJwtTokenException {

        try {
            Claims claims = this.parseClaims(refreshToken);
            return this.createAccessTokenOnly(claims);

        } catch (JwtException e) {
            throw new InvalidJwtTokenException();
        }
    }


    private TokenDto createAccessTokenOnly(Claims claims) {
        Date now = new Date();

        String accessToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidMilliSecond))
                .signWith(secretKey)
                .compact();

        return TokenDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken("")
                .accessTokenExpireDate(accessTokenValidMilliSecond)
                .build();
    }
}
