package com.inhabas.api.web.argumentResolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.inhabas.api.auth.domain.token.exception.TokenMissingException;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class LoginMemberArgumentResolverTest {

  private final LoginMemberArgumentResolver resolver = new LoginMemberArgumentResolver();

  @Mock private MethodParameter parameter;

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  private void setAuthentication(Object principal, String role) {
    Authentication authentication = mock(Authentication.class);
    doReturn(List.of(new SimpleGrantedAuthority(role))).when(authentication).getAuthorities();
    if (principal != null) {
      doReturn(principal).when(authentication).getPrincipal();
    }
    SecurityContext securityContext = mock(SecurityContext.class);
    given(securityContext.getAuthentication()).willReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
  }

  @DisplayName("@Authenticated 어노테이션이 붙은 파라미터만 지원한다.")
  @Test
  void supportsParameterTest() {
    // given
    given(parameter.hasParameterAnnotation(Authenticated.class)).willReturn(true);

    // when then
    assertThat(resolver.supportsParameter(parameter)).isTrue();
  }

  @DisplayName("@Authenticated 어노테이션이 없는 파라미터는 지원하지 않는다.")
  @Test
  void notSupportsParameterTest() {
    // given
    given(parameter.hasParameterAnnotation(Authenticated.class)).willReturn(false);

    // when then
    assertThat(resolver.supportsParameter(parameter)).isFalse();
  }

  @DisplayName("인증된 회원이면 memberId를 반환한다.")
  @Test
  void resolveArgumentTest() {
    // given
    setAuthentication(1L, "ROLE_BASIC");

    // when
    Object memberId = resolver.resolveArgument(parameter, null, null, null);

    // then
    assertThat(memberId).isEqualTo(1L);
  }

  @DisplayName("익명 사용자면 TokenMissingException을 던진다.")
  @Test
  void resolveArgumentAnonymousTest() {
    // given
    setAuthentication(null, "ROLE_ANONYMOUS");

    // when then
    assertThatThrownBy(() -> resolver.resolveArgument(parameter, null, null, null))
        .isInstanceOf(TokenMissingException.class);
  }
}
