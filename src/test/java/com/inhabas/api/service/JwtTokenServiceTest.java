package com.inhabas.api.service;

import com.inhabas.api.domain.member.type.wrapper.Role;
import com.inhabas.api.security.domain.RefreshToken;
import com.inhabas.api.security.domain.RefreshTokenNotFoundException;
import com.inhabas.api.security.domain.RefreshTokenRepository;
import com.inhabas.api.security.domain.TokenService;
import com.inhabas.api.security.jwtUtils.InvalidJwtTokenException;
import com.inhabas.api.security.jwtUtils.JwtTokenProvider;
import com.inhabas.api.security.jwtUtils.TokenDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
public class JwtTokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private HttpServletRequest request;

    @Spy
    private JwtTokenProvider tokenProvider;

    @DisplayName("refreshToken 을 저장한다.")
    @Test
    public void saveRefreshTokenTest() {
        //given
        RefreshToken refreshToken = new RefreshToken("header.body.signature");
        given(refreshTokenRepository.save(any(RefreshToken.class))).willReturn(refreshToken);

        //when
        tokenService.saveRefreshToken(refreshToken);

        then(refreshTokenRepository).should(times(1)).save(any(RefreshToken.class));
    }

    @DisplayName("accessToken 을 재발급한다.")
    @Test
    public void reissueAccessTokenTest() {
        //given
        String refreshTokenString = tokenProvider.createJwtToken(1, String.valueOf(Role.BASIC_MEMBER), null).getRefreshToken();
        given(request.getHeader(anyString())).willReturn("Bearer " + refreshTokenString);
        given(refreshTokenRepository.existsByRefreshToken(any())).willReturn(true);

        //when
        TokenDto tokenDto = tokenService.reissueAccessToken(request);

        assertThat(tokenDto.getAccessToken()).isNotBlank();
    }

    @DisplayName("db에 refreshToken 이 없으면, 유효한 토큰이어도 RefreshTokenNotFoundException 발생")
    @Test
    public void refreshTokenNotFoundExceptionTest() {
        //given
        String refreshTokenString = tokenProvider.createJwtToken(1, String.valueOf(Role.BASIC_MEMBER), null).getRefreshToken();
        given(request.getHeader(anyString())).willReturn("Bearer " + refreshTokenString);
        given(refreshTokenRepository.existsByRefreshToken(any())).willReturn(false);

        //when
        assertThrows(RefreshTokenNotFoundException.class,
                () -> tokenService.reissueAccessToken(request));
    }

    @DisplayName("유효하지 않은 refreshToken 은 InvalidJwtTokenException")
    @Test
    public void invalidRefreshTokenTest() {
        //given
        given(refreshTokenRepository.existsByRefreshToken(any())).willReturn(true);

        String refreshTokenString = tokenProvider.createJwtToken(1, String.valueOf(Role.BASIC_MEMBER), null).getRefreshToken();
        int length = refreshTokenString.length();
        String corruptedToken = refreshTokenString.substring(0, length - 3) + "corruptChar" + refreshTokenString.substring(length - 3);
        given(request.getHeader(anyString())).willReturn("Bearer " + corruptedToken);

        //when
        assertThrows(InvalidJwtTokenException.class,
                () -> tokenService.reissueAccessToken(request));
    }
}
