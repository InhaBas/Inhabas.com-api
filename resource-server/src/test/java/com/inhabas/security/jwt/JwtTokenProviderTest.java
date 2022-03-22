package com.inhabas.security.jwt;

import com.inhabas.api.security.utils.jwtUtils.InvalidJwtTokenException;
import com.inhabas.api.security.utils.jwtUtils.JwtTokenDecodedInfo;
import com.inhabas.api.security.utils.jwtUtils.JwtTokenProvider;
import com.inhabas.api.security.domain.token.TokenDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JwtTokenProviderTest {

    private JwtTokenProvider tokenProvider;

    @BeforeEach
    public void setUp() {
        tokenProvider = new JwtTokenProvider();
    }

    @DisplayName("access 토큰과 refresh 토큰을 발급한다.")
    @Test
    public void createJwtTokenTest() {
        //given
        Integer userId = 1;
        String role = "회장단";
        Set<String> teams = new HashSet<>() {{
            add("운영팀");
            add("IT팀");}
        };

        //when
        TokenDto newJwtToken = tokenProvider.createJwtToken(userId, role, teams);

        //then
        assertThat(newJwtToken).isNotNull();
        assertThat(newJwtToken.getAccessToken()
                .chars().filter(ch -> ch == '.')
                .count())
                .isEqualTo(2);
        assertThat(newJwtToken.getRefreshToken()
                .chars().filter(ch -> ch == '.')
                .count())
                .isEqualTo(2);
    }

    @DisplayName("토큰 생성 시 role 은 필수로 주어져야 한다.")
    @Test
    public void nullRoleTokenTest() {
        //given
        Integer userId = 1;
        String role = null;
        Set<String> teams = new HashSet<>() {{
            add("운영팀");
            add("IT팀");}
        };

        //when
        assertThrows(AssertionError.class,
                ()-> tokenProvider.createJwtToken(userId, role, teams));
    }

    @DisplayName("토큰 생성 시 authUserId 은 필수로 주어져야 한다.")
    @Test
    public void nullUserIdTokenTest() {
        //given
        Integer userId = null;
        String role = "회장단";
        Set<String> teams = new HashSet<>() {{
            add("운영팀");
            add("IT팀");}
        };

        //when
        assertThrows(AssertionError.class,
                ()-> tokenProvider.createJwtToken(userId, role, teams));
    }

    @DisplayName("토큰을 정상적으로 decode")
    @Test
    public void decodeToken() {
        //given
        Integer userId = 1;
        Set<String> teams = new HashSet<>() {{
            add("운영팀");
            add("IT팀");}
        };

        TokenDto newJwtToken = tokenProvider.createJwtToken(userId, "회장단", teams);
        String accessToken = newJwtToken.getAccessToken();
        String refreshToken = newJwtToken.getRefreshToken();

        //when
        JwtTokenDecodedInfo accessTokenDecodeInfo = tokenProvider.authenticate(accessToken);
        JwtTokenDecodedInfo refreshTokenDecodeInfo = tokenProvider.authenticate(refreshToken);

        //then
        //access token
        assertThat(accessTokenDecodeInfo.getAuthUserId()).isEqualTo(userId);
        assertThat(accessTokenDecodeInfo.getGrantedAuthorities())
                .extracting("role")
                .contains("ROLE_회장단")
                .containsAll(teams);
        //refresh token
        assertThat(refreshTokenDecodeInfo.getAuthUserId()).isEqualTo(userId);
        assertThat(refreshTokenDecodeInfo.getGrantedAuthorities())
                .extracting("role")
                .contains("ROLE_회장단")
                .containsAll(teams);
    }

    @DisplayName("access 토큰을 재발급한다.")
    @Test
    public void reissueAccessToken() {
        //given
        Integer userId = 1;
        Set<String> teams = new HashSet<>() {{
            add("운영팀");
            add("IT팀");}
        };

        TokenDto newJwtToken = tokenProvider.createJwtToken(userId, "회장단", teams);
        String refreshToken = newJwtToken.getRefreshToken();

        //when
        TokenDto newTokenDto = tokenProvider.reissueAccessTokenUsing(refreshToken);

        //then
        //check new token dto
        assertThat(newTokenDto.getRefreshToken()).isBlank();
        assertThat(newTokenDto.getExpiresIn()).isNotNull();
        assertThat(newTokenDto.getGrantType()).isEqualTo("Bearer");
        String newAccessToken = newTokenDto.getAccessToken();
        assertThat(newAccessToken)
                .isNotBlank();

        //validation check for newly issued access token
        JwtTokenDecodedInfo decodeNewAccessToken = tokenProvider.authenticate(newAccessToken);
        assertThat(decodeNewAccessToken.getAuthUserId()).isEqualTo(userId);
        assertThat(decodeNewAccessToken.getGrantedAuthorities())
                .extracting("role")
                .contains("ROLE_회장단")
                .containsAll(teams);
    }

    @DisplayName("refresh 토큰이 유효하지 않아서 재발급에 실패한다.")
    @Test
    public void failToIssueNewAccessToken() {

        assertThrows(InvalidJwtTokenException.class,
                ()-> tokenProvider.reissueAccessTokenUsing("invalid-token-string"));

    }

}
