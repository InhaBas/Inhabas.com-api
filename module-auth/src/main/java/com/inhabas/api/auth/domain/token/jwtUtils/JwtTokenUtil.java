package com.inhabas.api.auth.domain.token.jwtUtils;

import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfo;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfoFactory;
import com.inhabas.api.auth.domain.token.TokenDto;
import com.inhabas.api.auth.domain.token.TokenUtil;
import com.inhabas.api.auth.domain.token.exception.InvalidTokenException;
import com.inhabas.api.auth.domain.token.jwtUtils.refreshToken.RefreshToken;
import com.inhabas.api.auth.domain.token.jwtUtils.refreshToken.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

// https://github.com/jwtk/jjwt
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtil implements TokenUtil {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.secretKey}")
    private String SECRET_KEY;
    private final Long ACCESS_TOKEN_VALID_MILLISECOND = 30 * 60 * 1000L; // 0.5 hour
    private static final Long REFRESH_TOKEN_VALID_MILLI_SECOND = 7 * 24 * 60 * 60 * 1000L; // 7 days
    private static final String PROVIDER = "provider";
    private static final String AUTHORITY = "authorities";
    private static final String EMAIL = "email";
    private static final String NAME = "name";


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

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo((OAuth2AuthenticationToken) authentication);
        String provider = oAuth2UserInfo.getProvider().toString();
        String uid = oAuth2UserInfo.getId();
        String email = oAuth2UserInfo.getEmail();
        String name = oAuth2UserInfo.getName();

        List<String> authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(uid)
                .claim(PROVIDER, provider)
                .claim(NAME, name)
                .claim(EMAIL, email)
                .claim(AUTHORITY, authorities)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }


    @Override
    public boolean validate(String token) {

        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (SignatureException ex) {
            log.error("JWT signature does not match");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

    /* web request 에 대한 인증 정보를 반환함. */
    @Override
    @SuppressWarnings("unchecked")
    public JwtAuthenticationResult getAuthentication(String token) throws JwtException {

        Claims claims = this.parseClaims(token);

        String uid = claims.getSubject();
        String provider = claims.get(PROVIDER, String.class);
        String email = claims.get(EMAIL, String.class);
        List<? extends GrantedAuthority> grantedAuthorities =
                (List<SimpleGrantedAuthority>) claims.get(AUTHORITY, List.class).stream()
                        .map(authority-> new SimpleGrantedAuthority((String) authority))
                        .collect(Collectors.toList());

        return new JwtAuthenticationResult(uid, provider, email, grantedAuthorities);
    }


    /* 토큰 body 에 넣어둔 사용자 정보를 가져옴
     * validation 검사를 먼저 꼭 해야함! */
    private Claims parseClaims(String token) throws JwtException {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
    }

    @Override
    public TokenDto reissueAccessTokenUsing(String refreshToken) throws InvalidTokenException {

        try {
            Claims claims = this.parseClaims(refreshToken);
            return this.createAccessTokenOnly(claims);

        } catch (JwtException e) {
            throw new InvalidTokenException();
        }
    }


    private TokenDto createAccessTokenOnly(Claims claims) {
        Date now = new Date();

        String accessToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_MILLISECOND))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();

        return TokenDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken("")
                .accessTokenExpireDate(ACCESS_TOKEN_VALID_MILLISECOND)
                .build();
    }
}
