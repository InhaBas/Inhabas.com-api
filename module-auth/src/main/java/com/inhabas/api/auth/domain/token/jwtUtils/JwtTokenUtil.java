package com.inhabas.api.auth.domain.token.jwtUtils;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

import com.inhabas.api.auth.domain.oauth2.CustomOAuth2User;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfo;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfoFactory;
import com.inhabas.api.auth.domain.token.TokenDto;
import com.inhabas.api.auth.domain.token.TokenUtil;
import com.inhabas.api.auth.domain.token.exception.ExpiredTokenException;
import com.inhabas.api.auth.domain.token.exception.InvalidTokenException;
import com.inhabas.api.auth.domain.token.jwtUtils.refreshToken.RefreshToken;
import com.inhabas.api.auth.domain.token.jwtUtils.refreshToken.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.security.WeakKeyException;

// https://github.com/jwtk/jjwt
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtil implements TokenUtil {

  private final RefreshTokenRepository refreshTokenRepository;

  @Value("${jwt.secretKey}")
  private String SECRET_KEY;

  @Value("${jwt.issuer:inhabas.com}")
  private String ISSUER;

  @Value("${jwt.audience:inhabas-client}")
  private String AUDIENCE;

  private final Long ACCESS_TOKEN_VALID_MILLIS = 30 * 60 * 1000L; // 0.5 hour
  private static final Long REFRESH_TOKEN_VALID_MILLIS = 7 * 24 * 60 * 60 * 1000L; // 7 days
  private static final String AUTHORITY = "authorities";
  private static final String MEMBER_ID = "memberId";
  private static final String MEMBER_NAME = "memberName";
  private static final String MEMBER_PICTURE = "memberPicture";
  private static final int MIN_HS512_KEY_BYTES = 64; // 512 bits

  // 캐시된 키와 파서
  private volatile Key signingKey;
  private volatile JwtParser jwtParser;

  @PostConstruct
  public void init() {
    validateSecretKeyStrength();
  }

  /** HS512 용 시크릿 키 강도 검증 및 Key/Parser 캐싱. */
  public void validateSecretKeyStrength() {
    if (SECRET_KEY == null || SECRET_KEY.trim().isEmpty()) {
      throw new IllegalStateException("jwt.secretKey is not configured or blank");
    }

    final byte[] decoded;
    try {
      decoded = Decoders.BASE64.decode(SECRET_KEY.trim());
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("jwt.secretKey must be Base64-encoded", e);
    }

    if (decoded.length < MIN_HS512_KEY_BYTES) {
      throw new IllegalStateException(
          "Weak jwt.secretKey for HS512: require >= 64 bytes after Base64 decoding, got "
              + decoded.length
              + " bytes");
    }

    try {
      // 키/파서 캐시 생성 (WeakKeyException 등 조기 검출)
      Key key = Keys.hmacShaKeyFor(decoded);
      this.signingKey = key;
      this.jwtParser =
          Jwts.parserBuilder()
              .setSigningKey(key)
              .requireIssuer(ISSUER)
              .requireAudience(AUDIENCE)
              .build();
    } catch (WeakKeyException e) {
      throw new IllegalStateException("Weak jwt.secretKey: " + e.getMessage(), e);
    }
  }

  private Key getSigningKey() {
    Key key = this.signingKey;
    if (key != null) {
      return key;
    }
    // lazy-init (테스트 등 부팅 훅 없이 사용되는 경우 대비)
    Key newKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    this.signingKey = newKey;
    return newKey;
  }

  private JwtParser getJwtParser() {
    JwtParser parser = this.jwtParser;
    if (parser != null) {
      return parser;
    }
    // lazy-init
    JwtParser newParser =
        Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .requireIssuer(ISSUER)
            .requireAudience(AUDIENCE)
            .build();
    this.jwtParser = newParser;
    return newParser;
  }

  @Override
  public String createAccessToken(Authentication authentication) {

    return createToken(authentication, ACCESS_TOKEN_VALID_MILLIS);
  }

  @Override
  public String createRefreshToken(Authentication authentication) {

    String token = this.createToken(authentication, REFRESH_TOKEN_VALID_MILLIS);
    refreshTokenRepository.save(new RefreshToken(token));
    return token;
  }

  @Override
  public Long getExpiration() {
    // 초 단위로 통일
    return this.ACCESS_TOKEN_VALID_MILLIS / 1000;
  }

  private String createToken(Authentication authentication, Long expiration) {

    if (authentication == null) {
      throw new IllegalArgumentException("authentication must not be null");
    }

    OAuth2UserInfo oAuth2UserInfo =
        OAuth2UserInfoFactory.getOAuth2UserInfo((OAuth2AuthenticationToken) authentication);
    String uid = oAuth2UserInfo.getId();

    CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

    List<String> authorities =
        authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expiration);
    final Key key = getSigningKey();

    return Jwts.builder()
        .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
        .setIssuer(ISSUER)
        .setAudience(AUDIENCE)
        .setId(UUID.randomUUID().toString())
        .setSubject(uid)
        .claim(MEMBER_ID, customOAuth2User.getMemberId())
        .claim(MEMBER_NAME, customOAuth2User.getMemberName())
        .claim(MEMBER_PICTURE, customOAuth2User.getMemberPicture())
        .claim(AUTHORITY, authorities)
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(key, SignatureAlgorithm.HS512)
        .compact();
  }

  @Override
  public void validate(String token) {

    try {
      getJwtParser().parseClaimsJws(token);
    } catch (SecurityException ex) {
      log.error("Invalid JWT signature");
      throw new InvalidTokenException();
    } catch (MalformedJwtException ex) {
      log.error("Invalid JWT token");
      throw new InvalidTokenException();
    } catch (ExpiredJwtException ex) {
      log.error("Expired JWT token");
      throw new ExpiredTokenException();
    } catch (UnsupportedJwtException ex) {
      log.error("Unsupported JWT token");
      throw new InvalidTokenException();
    } catch (SignatureException ex) {
      log.error("JWT signature does not match");
      throw new InvalidTokenException();
    } catch (IllegalArgumentException ex) {
      log.error("JWT claims string is empty.");
      throw new InvalidTokenException();
    }
  }

  /* web request 에 대한 인증 정보를 반환함. */
  @Override
  public JwtAuthenticationToken getAuthentication(String token) throws JwtException {

    Claims claims = this.parseClaims(token);

    Number memberIdNumber = claims.get(MEMBER_ID, Number.class);
    Long memberId = memberIdNumber == null ? null : memberIdNumber.longValue();

    List<?> rawAuthorities = claims.get(AUTHORITY, List.class);
    if (rawAuthorities == null) rawAuthorities = List.of();

    List<SimpleGrantedAuthority> grantedAuthorities =
        rawAuthorities.stream()
            .filter(authority -> authority instanceof String)
            .map(authority -> new SimpleGrantedAuthority((String) authority))
            .collect(Collectors.toList());

    return JwtAuthenticationToken.of(memberId, token, grantedAuthorities);
  }

  /* 토큰 body 에 넣어둔 사용자 정보를 가져옴
   * validation 검사를 먼저 꼭 해야함! */
  private Claims parseClaims(String token) throws JwtException {
    return getJwtParser().parseClaimsJws(token).getBody();
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
    final Key key = getSigningKey();

    // 화이트리스트 기반으로 클레임 재구성: 필요한 필드만 복사
    String subject = claims.getSubject();
    Number memberIdNumber = claims.get(MEMBER_ID, Number.class);
    Long memberId = memberIdNumber == null ? null : memberIdNumber.longValue();
    String memberName = claims.get(MEMBER_NAME, String.class);
    String memberPicture = claims.get(MEMBER_PICTURE, String.class);
    List<?> rawAuthorities = claims.get(AUTHORITY, List.class);

    List<String> authorities =
        rawAuthorities == null
            ? List.of()
            : rawAuthorities.stream()
                .filter(v -> v instanceof String)
                .map(v -> (String) v)
                .collect(Collectors.toList());

    String accessToken =
        Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setIssuer(ISSUER)
            .setAudience(AUDIENCE)
            .setId(UUID.randomUUID().toString())
            .setSubject(subject)
            .claim(MEMBER_ID, memberId)
            .claim(MEMBER_NAME, memberName)
            .claim(MEMBER_PICTURE, memberPicture)
            .claim(AUTHORITY, authorities)
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_MILLIS))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();

    return TokenDto.builder()
        .grantType("Bearer")
        .accessToken(accessToken)
        .refreshToken("")
        .accessTokenExpireDate(ACCESS_TOKEN_VALID_MILLIS / 1000) // 초 단위
        .build();
  }
}
