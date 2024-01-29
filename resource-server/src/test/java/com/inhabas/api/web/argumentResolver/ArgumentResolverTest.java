package com.inhabas.api.web.argumentResolver;

import static org.assertj.core.api.Assertions.assertThat;

import com.inhabas.api.auth.domain.token.jwtUtils.JwtAuthenticationToken;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.NativeWebRequest;

@ExtendWith(MockitoExtension.class)
public class ArgumentResolverTest {

  @Mock private MethodParameter parameter;

  @Mock private NativeWebRequest request;

  @InjectMocks private LoginMemberArgumentResolver loginMemberArgumentResolver;

  @AfterEach
  public void clearSecurityContest() {
    SecurityContextHolder.clearContext();
  }

  @DisplayName("인증이 이루어지지 않았으면, null을 반환한다.")
  @Test
  public void returnNullIfNotAuthenticated() {
    // when
    Object invalidUser =
        loginMemberArgumentResolver.resolveArgument(parameter, null, request, null);

    // then
    assertThat(invalidUser).isNull();
  }

  @DisplayName("Jwt 토큰 인증된 회원의 Member Id를 반환한다.")
  @Test
  public void successToInjectJwtTokenIntegerIdIntoArguments() {
    // given
    Long memberId = 1L;

    // jwt 토큰 인증 결과
    List<? extends GrantedAuthority> grantedAuthorities =
        List.of(new SimpleGrantedAuthority("ROLE_TEST"));
    JwtAuthenticationToken authentication =
        JwtAuthenticationToken.of(1L, "Nodata", grantedAuthorities);

    // authentication 객체를 컨텍스트에 설정. 최종 인증 끝
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // when
    Object profileId = loginMemberArgumentResolver.resolveArgument(parameter, null, request, null);

    // then
    Assertions.assertThat(profileId).isNotNull();
    Assertions.assertThat(profileId).isEqualTo(memberId);
    Assertions.assertThat(profileId).isInstanceOf(Long.class);
  }
}
