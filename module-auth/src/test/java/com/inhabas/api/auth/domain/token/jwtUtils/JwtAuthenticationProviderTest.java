package com.inhabas.api.auth.domain.token.jwtUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.inhabas.api.auth.domain.token.exception.InvalidTokenException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationProviderTest {

  @InjectMocks private JwtAuthenticationProvider jwtAuthenticationProvider;

  @Mock private JwtTokenUtil jwtTokenUtil;

  @DisplayName("토큰에서 추출한 principal 로 인증 결과를 반환한다.")
  @Test
  public void authenticateTest() {
    // given
    String token = "header.payload.signature";
    JwtAuthenticationToken authRequest = JwtAuthenticationToken.of(token);
    JwtAuthenticationToken authResult =
        JwtAuthenticationToken.of(1L, token, List.of(new SimpleGrantedAuthority("ROLE_BASIC")));
    given(jwtTokenUtil.getAuthentication(token)).willReturn(authResult);

    // when
    Authentication authentication = jwtAuthenticationProvider.authenticate(authRequest);

    // then
    assertThat(authentication).isSameAs(authResult);
    then(jwtTokenUtil).should().getAuthentication(token);
  }

  @DisplayName("유효하지 않은 토큰이면 InvalidTokenException 이 전파된다.")
  @Test
  public void authenticateFailTest() {
    // given
    String token = "invalid.token";
    JwtAuthenticationToken authRequest = JwtAuthenticationToken.of(token);
    given(jwtTokenUtil.getAuthentication(token)).willThrow(new InvalidTokenException());

    // when then
    assertThrows(
        InvalidTokenException.class, () -> jwtAuthenticationProvider.authenticate(authRequest));
  }

  @DisplayName("JwtAuthenticationToken 타입만 지원한다.")
  @Test
  public void supportsTest() {
    assertThat(jwtAuthenticationProvider.supports(JwtAuthenticationToken.class)).isTrue();
    assertThat(jwtAuthenticationProvider.supports(UsernamePasswordAuthenticationToken.class))
        .isFalse();
  }
}
