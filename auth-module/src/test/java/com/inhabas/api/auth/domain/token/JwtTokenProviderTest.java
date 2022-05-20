package com.inhabas.api.auth.domain.token;

import com.inhabas.api.auth.domain.token.jwtUtils.JwtAuthenticationResult;
import com.inhabas.api.auth.domain.token.jwtUtils.JwtTokenProvider;
import com.inhabas.api.auth.domain.token.jwtUtils.refreshToken.RefreshTokenRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtTokenProviderTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private JwtTokenProvider tokenProvider;


    @DisplayName("access 토큰을 발급한다.")
    @Test
    public void createJwtTokenTest() {
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
                new DefaultOAuth2User(authorities, attributes, "sub"), authorities, "google");

        //when
        String accessToken = tokenProvider.createAccessToken(authentication);

        //then
        assertThat(accessToken).isNotNull();
        assertThat(accessToken.chars()
                .filter(ch -> ch == '.')
                .count())
                .isEqualTo(2);
    }


    @DisplayName("토큰 생성 시 인증결과객체는 필수로 주어져야 한다.")
    @Test
    public void nullMemberIdTokenTest() {

        assertThrows(AssertionError.class,
                () -> tokenProvider.createAccessToken(null));
    }


    @DisplayName("토큰을 정상적으로 decode")
    @Test
    public void decodeToken() {
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
                new DefaultOAuth2User(authorities, attributes, "sub"), authorities, "google");

        String accessToken = tokenProvider.createAccessToken(authentication);

        //when
        JwtAuthenticationResult authenticationToken = tokenProvider.decode(accessToken);

        //then
        assertThat(authenticationToken.getProvider()).isEqualTo("google");
        assertThat(authenticationToken.getUid()).isEqualTo("1249846925629348");
        assertThat(authenticationToken.getAuthorities()).isEqualTo(authorities);
    }

    @Disabled
    @DisplayName("access 토큰을 재발급한다.")
    @Test
    public void reissueAccessToken() {
        //given
        Integer userId = 1;
        Integer memberSocialAccountId = 21;
        Set<String> teams = new HashSet<>() {
            {
                add("운영팀");
                add("IT팀");
            }
        };

//        TokenDto newJwtToken = tokenProvider.createAccessToken(userId, memberSocialAccountId, "회장단", teams);
//        String refreshToken = newJwtToken.getRefreshToken();
//
//        //when
//        TokenDto newTokenDto = tokenProvider.reissueAccessTokenUsing(refreshToken);
//
//        //then
//        //check new token dto
//        assertThat(newTokenDto.getRefreshToken()).isBlank();
//        assertThat(newTokenDto.getExpiresIn()).isNotNull();
//        assertThat(newTokenDto.getGrantType()).isEqualTo("Bearer");
//        String newAccessToken = newTokenDto.getAccessToken();
//        Assertions.assertThat(newAccessToken)
//                .isNotBlank();
//
//        //validation check for newly issued access token
//        JwtTokenDecodedInfo decodeNewAccessToken = tokenProvider.authenticate(newAccessToken);
//        assertThat(decodeNewAccessToken.getMemberId()).isEqualTo(userId);
//        assertThat(decodeNewAccessToken.getMemberSocialAccountId()).isEqualTo(memberSocialAccountId);
//        assertThat(decodeNewAccessToken.getGrantedAuthorities())
//                .extracting("role")
//                .contains("ROLE_회장단")
//                .containsAll(teams);
    }

    @DisplayName("유효하지 않은 토큰 string 을 검사한다.")
    @Test
    public void validateInvalidToken() {

        assertFalse(tokenProvider.validate("invalid-token-string"));
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
                new DefaultOAuth2User(authorities, attributes, "sub"), authorities, "google");

        String accessToken = tokenProvider.createAccessToken(authentication);

        //then
        assertTrue(tokenProvider.validate(accessToken));
    }
}
