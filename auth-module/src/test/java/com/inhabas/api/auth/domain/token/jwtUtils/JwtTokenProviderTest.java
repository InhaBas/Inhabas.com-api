package com.inhabas.api.auth.domain.token.jwtUtils;

import com.inhabas.api.auth.domain.token.TokenDto;
import org.assertj.core.api.Assertions;
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
        Integer memberId = 1;
        Integer memberSocialAccountId = 21;
        String role = "회장단";
        Set<String> teams = new HashSet<>() {{
            add("운영팀");
            add("IT팀");}
        };

        //when
        TokenDto newJwtToken = tokenProvider.createJwtToken(memberId, memberSocialAccountId, role, teams);

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
        Integer memberId = 1;
        Integer memberSocialAccountId = 21;
        String role = null;
        Set<String> teams = new HashSet<>() {{
            add("운영팀");
            add("IT팀");}
        };

        //when
        assertThrows(AssertionError.class,
                ()-> tokenProvider.createJwtToken(memberId, memberSocialAccountId, role, teams));
    }

    @DisplayName("토큰 생성 시 memberId 은 필수로 주어져야 한다.")
    @Test
    public void nullMemberIdTokenTest() {
        //given
        Integer memberId = null;
        Integer memberSocialAccountId = 21;
        String role = "회장단";
        Set<String> teams = new HashSet<>() {{
            add("운영팀");
            add("IT팀");}
        };

        //when
        assertThrows(AssertionError.class,
                ()-> tokenProvider.createJwtToken(memberId, memberSocialAccountId, role, teams));
    }

    @DisplayName("토큰을 정상적으로 decode")
    @Test
    public void decodeToken() {
        //given
        Integer memberId = 1;
        Integer memberSocialAccountId = 21;
        Set<String> teams = new HashSet<>() {{
            add("운영팀");
            add("IT팀");}
        };

        TokenDto newJwtToken = tokenProvider.createJwtToken(memberId, memberSocialAccountId, "회장단", teams);
        String accessToken = newJwtToken.getAccessToken();
        String refreshToken = newJwtToken.getRefreshToken();

        //when
        JwtTokenDecodedInfo accessTokenDecodeInfo = tokenProvider.authenticate(accessToken);
        JwtTokenDecodedInfo refreshTokenDecodeInfo = tokenProvider.authenticate(refreshToken);

        //then
        //access token
        assertThat(accessTokenDecodeInfo.getMemberId()).isEqualTo(memberId);
        assertThat(accessTokenDecodeInfo.getMemberSocialAccountId()).isEqualTo(memberSocialAccountId);
        assertThat(accessTokenDecodeInfo.getGrantedAuthorities())
                .extracting("role")
                .contains("ROLE_회장단")
                .containsAll(teams);
        //refresh token
        assertThat(refreshTokenDecodeInfo.getMemberId()).isEqualTo(memberId);
        assertThat(refreshTokenDecodeInfo.getMemberSocialAccountId()).isEqualTo(memberSocialAccountId);
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
        Integer memberSocialAccountId = 21;
        Set<String> teams = new HashSet<>() {{
            add("운영팀");
            add("IT팀");}
        };

        TokenDto newJwtToken = tokenProvider.createJwtToken(userId, memberSocialAccountId, "회장단", teams);
        String refreshToken = newJwtToken.getRefreshToken();

        //when
        TokenDto newTokenDto = tokenProvider.reissueAccessTokenUsing(refreshToken);

        //then
        //check new token dto
        assertThat(newTokenDto.getRefreshToken()).isBlank();
        assertThat(newTokenDto.getExpiresIn()).isNotNull();
        assertThat(newTokenDto.getGrantType()).isEqualTo("Bearer");
        String newAccessToken = newTokenDto.getAccessToken();
        Assertions.assertThat(newAccessToken)
                .isNotBlank();

        //validation check for newly issued access token
        JwtTokenDecodedInfo decodeNewAccessToken = tokenProvider.authenticate(newAccessToken);
        assertThat(decodeNewAccessToken.getMemberId()).isEqualTo(userId);
        assertThat(decodeNewAccessToken.getMemberSocialAccountId()).isEqualTo(memberSocialAccountId);
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
