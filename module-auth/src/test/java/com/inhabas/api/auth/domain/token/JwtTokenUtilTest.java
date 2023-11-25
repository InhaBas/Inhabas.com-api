package com.inhabas.api.auth.domain.token;

import com.inhabas.api.auth.domain.oauth2.CustomOAuth2User;
import com.inhabas.api.auth.domain.token.jwtUtils.JwtAuthenticationToken;
import com.inhabas.api.auth.domain.token.jwtUtils.JwtTokenUtil;
import com.inhabas.api.auth.domain.token.jwtUtils.refreshToken.RefreshTokenRepository;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtTokenUtilTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtTokenUtil, "SECRET_KEY", "TESTTESTTESTTESTTESTTESTTESTTEST" +
                "TESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTEST");
    }

    @DisplayName("access 토큰을 발급한다.")
    @Test
    public void createJwtTokenTest() {
        //given
        List<SimpleGrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS"));
        Map<String, Object> attributes = new HashMap<>() {{
            put("sub", "1249846925629348");
            put("name", "유동현");
            put("picture", "blahblah");
            put("email", "my@gmail.com");
            put("locale", "ko");
        }};
        OAuth2AuthenticationToken authentication = new OAuth2AuthenticationToken(
                new CustomOAuth2User(authorities, attributes, "sub", 1L),
                authorities, "google");



        //when
        String accessToken = jwtTokenUtil.createAccessToken(authentication);

        //then
        assertThat(accessToken).isNotNull();
        assertThat(accessToken.chars()
                .filter(ch -> ch == '.')
                .count())
                .isEqualTo(2);
    }


    @DisplayName("토큰 생성 시 인증결과객체는 필수로 주어져야 한다.")
    @Test
    public void nullAuthenticationTokenTest() {

        assertThrows(AssertionError.class,
                () -> jwtTokenUtil.createAccessToken(null));
    }


    @DisplayName("토큰을 정상적으로 decode")
    @Test
    public void getAuthenticationUsingToken() {
        //given
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS"));
        Map<String, Object> attributes = new HashMap<>() {{
            put("sub", "1249846925629348");
            put("name", "유동현");
            put("picture", "blahblah");
            put("email", "my@gmail.com");
            put("locale", "ko");
        }};
        OAuth2AuthenticationToken authentication = new OAuth2AuthenticationToken(
                new CustomOAuth2User(authorities, attributes, "sub",1L),
                authorities, "google");

        String accessToken = jwtTokenUtil.createAccessToken(authentication);

        //when
        JwtAuthenticationToken authenticationToken = jwtTokenUtil.getAuthentication(accessToken);

        //then
        assertThat(authenticationToken.getPrincipal()).isEqualTo(1L);
        assertThat(authenticationToken.getAuthorities()).isEqualTo(authorities);
    }


    @DisplayName("access 토큰을 재발급한다.")
    @Test
    public void reissueAccessToken() {

        //given
        List<SimpleGrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_USER"));
        Map<String, Object> attributes = new HashMap<>() {{
            put("sub", "1234567889");
            put("name", "유동현");
            put("given_name", "동현");
            put("family_name", "유");
            put("email_verified", true);
            put("picture", "blahblah");
            put("email", "my@gmail.com");
            put("locale", "ko");
        }};
        OAuth2AuthenticationToken authentication = new OAuth2AuthenticationToken(
                new CustomOAuth2User(authorities, attributes, "sub",1L),
                authorities,
                "google");
        String refreshToken = jwtTokenUtil.createRefreshToken(authentication);

        //when
        TokenDto newTokenDto = jwtTokenUtil.reissueAccessTokenUsing(refreshToken);

        //then
        //check new token dto
        assertThat(newTokenDto.getRefreshToken()).isBlank();
        assertThat(newTokenDto.getExpiresIn()).isNotNull();
        assertThat(newTokenDto.getGrantType()).isEqualTo("Bearer");
        String newAccessToken = newTokenDto.getAccessToken();
        Assertions.assertThat(newAccessToken)
                .isNotBlank();

        //validation check for newly issued access token
        JwtAuthenticationToken authenticateNewAccessToken = jwtTokenUtil.getAuthentication(newAccessToken);


        assertThat(authenticateNewAccessToken.getPrincipal()).isEqualTo(1L);
        assertThat(authenticateNewAccessToken.getAuthorities()).isEqualTo(authorities);
        assertThat(authenticateNewAccessToken.getAuthorities())
                .extracting("role")
                .contains("ROLE_USER");

    }

    @DisplayName("유효하지 않은 토큰 string 을 검사한다.")
    @Test
    public void validateInvalidToken() {

        assertFalse(jwtTokenUtil.validate("invalid-token-string"));
    }

    @DisplayName("유효한 토큰 string 을 검사한다.")
    @Test
    public void validateValidToken() {
        //given
        List<SimpleGrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS"), new SimpleGrantedAuthority("TEAM_IT"));
        Map<String, Object> attributes = new HashMap<>() {{
            put("sub", "1249846925629348");
            put("name", "유동현");
            put("picture", "blahblah");
            put("email", "my@gmail.com");
            put("locale", "ko");
        }};
        OAuth2AuthenticationToken authentication = new OAuth2AuthenticationToken(
                new CustomOAuth2User(authorities, attributes, "sub",1L),
                authorities, "google");

        String accessToken = jwtTokenUtil.createAccessToken(authentication);

        //then
        assertTrue(jwtTokenUtil.validate(accessToken));
    }
}
