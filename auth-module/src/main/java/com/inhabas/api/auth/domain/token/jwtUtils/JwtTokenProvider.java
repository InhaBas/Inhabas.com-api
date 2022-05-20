package com.inhabas.api.auth.domain.token.jwtUtils;

import com.inhabas.api.auth.domain.token.TokenDto;
import com.inhabas.api.auth.domain.token.TokenProvider;
import com.inhabas.api.auth.domain.token.jwtUtils.refreshToken.RefreshToken;
import com.inhabas.api.auth.domain.token.jwtUtils.refreshToken.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.*;

// https://github.com/jwtk/jjwt
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider implements TokenProvider {

    private final RefreshTokenRepository refreshTokenRepository;

    // spring boot 가 실행될 때마다 secretKey 가 매번 달라짐.
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private final Long ACCESS_TOKEN_VALID_MILLISECOND = 30 * 60 * 1000L; // 0.5 hour
    private static final Long REFRESH_TOKEN_VALID_MILLI_SECOND = 7 * 24 * 60 * 60 * 1000L; // 7 days
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String MEMBER_SOCIAL_ACCOUNT_ID = "muId";
    private static final String PROVIDER = "provider";
    private static final String AUTHORITY = "authorities";


    @Override
    public String createAccessToken(Authentication authentication) {

        return createToken(authentication, ACCESS_TOKEN_VALID_MILLISECOND);
    }


    @Override
    public String createRefreshToken(Authentication authentication) {

        String token = this.createToken(authentication, REFRESH_TOKEN_VALID_MILLI_SECOND);
        refreshTokenRepository.save(new RefreshToken(token));
        return token;
    }


    @Override
    public Long getExpiration() {
        return this.ACCESS_TOKEN_VALID_MILLISECOND / 1000;
    }


    private String createToken(Authentication authentication, Long expiration) {

        assert authentication != null;

        String uid  = ((DefaultOAuth2User) authentication.getPrincipal()).getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String provider = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(uid)
                .claim(PROVIDER, provider)
                .claim(AUTHORITY, authorities)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SECRET_KEY)
                .compact();
    }


    /* web request 에 대한 인증 정보를 반환함. */
    @Override
    @SuppressWarnings("unchecked")
    public JwtTokenDecodedInfo authenticate(String token) throws JwtException {

        validateToken(token); // if not valid, it will throw jwtException

        Claims claims = this.parseClaims(token);
        Integer userId = Integer.parseInt(claims.getSubject());
        Integer muId = (Integer) claims.get(MEMBER_SOCIAL_ACCOUNT_ID);
        String role = (String) claims.get("ROLE");
        List<String> teams = (ArrayList<String>) claims.get("TEAM");

        Set<GrantedAuthority> totalGrantedAuthorities = new HashSet<>();
        totalGrantedAuthorities.add(new SimpleGrantedAuthority(role));
        if (teams != null) {
            teams.forEach(
                    teamAuthority -> totalGrantedAuthorities.add(new SimpleGrantedAuthority(teamAuthority)));
        }

        return new JwtTokenDecodedInfo(userId, muId, totalGrantedAuthorities);
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
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
        } catch (JwtException e) {
            log.debug(e.getMessage());
            throw new InvalidJwtTokenException();
        }
    }

    /* 토큰 body 에 넣어둔 사용자 정보를 가져옴
     * validation 검사를 먼저 꼭 해야함! */
    private Claims parseClaims(String token) throws JwtException {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
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
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_MILLISECOND))
                .signWith(SECRET_KEY)
                .compact();

        return TokenDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken("")
                .accessTokenExpireDate(ACCESS_TOKEN_VALID_MILLISECOND)
                .build();
    }
}
