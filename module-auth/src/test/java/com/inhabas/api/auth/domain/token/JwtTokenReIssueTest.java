package com.inhabas.api.auth.domain.token;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.HttpServletRequest;

import com.inhabas.api.auth.domain.token.exception.InvalidTokenException;
import com.inhabas.api.auth.domain.token.jwtUtils.JwtTokenReIssuer;
import com.inhabas.api.auth.domain.token.jwtUtils.JwtTokenUtil;
import com.inhabas.api.auth.domain.token.jwtUtils.refreshToken.RefreshTokenNotFoundException;
import com.inhabas.api.auth.domain.token.jwtUtils.refreshToken.RefreshTokenRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class JwtTokenReIssueTest {

  @InjectMocks private JwtTokenReIssuer tokenReIssuer;

  @Mock private RefreshTokenRepository refreshTokenRepository;

  @Mock private HttpServletRequest request;

  @Mock private TokenResolver tokenResolver;

  @Mock private JwtTokenUtil jwtTokenUtil;

  @DisplayName("accessToken 을 재발급한다.")
  @Test
  public void reissueAccessTokenTest() {

    // given
    given(refreshTokenRepository.existsByRefreshToken(any())).willReturn(true);

    // when
    tokenReIssuer.reissueAccessToken("refreshToken");

    // then
    then(jwtTokenUtil).should(times(1)).reissueAccessTokenUsing(any());
  }

  @DisplayName("db에 refreshToken 이 없으면, 유효한 토큰이어도 RefreshTokenNotFoundException 발생")
  @Test
  public void refreshTokenNotFoundExceptionTest() {
    // given
    given(refreshTokenRepository.existsByRefreshToken(any())).willReturn(false);

    // when
    assertThrows(
        RefreshTokenNotFoundException.class,
        () -> tokenReIssuer.reissueAccessToken("refreshToken"));
  }

  @DisplayName("유효하지 않은 refreshToken 은 InvalidJwtTokenException")
  @Test
  public void invalidRefreshTokenTest() {
    // given
    doThrow(InvalidTokenException.class).when(jwtTokenUtil).validate(any());

    // then
    assertThrows(
        InvalidTokenException.class, () -> tokenReIssuer.reissueAccessToken("refreshToken"));
  }
}
