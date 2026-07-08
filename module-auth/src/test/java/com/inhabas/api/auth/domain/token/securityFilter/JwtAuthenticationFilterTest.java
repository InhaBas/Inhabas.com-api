package com.inhabas.api.auth.domain.token.securityFilter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inhabas.api.auth.domain.token.TokenResolver;
import com.inhabas.api.auth.domain.token.exception.InvalidTokenException;
import com.inhabas.api.auth.domain.token.jwtUtils.JwtAuthenticationToken;
import com.inhabas.api.auth.domain.token.jwtUtils.JwtTokenUtil;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {

  private static final String TOKEN = "header.payload.signature";

  @Mock private JwtTokenUtil jwtTokenUtil;

  @Mock private TokenResolver tokenResolver;

  @Mock private AuthenticationManager authenticationManager;

  @Mock private FilterChain filterChain;

  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @BeforeEach
  public void setUp() {
    SecurityContextHolder.clearContext();
    jwtAuthenticationFilter =
        new JwtAuthenticationFilter(AnyRequestMatcher.INSTANCE, jwtTokenUtil, tokenResolver);
    jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
  }

  @AfterEach
  public void tearDown() {
    SecurityContextHolder.clearContext();
  }

  @DisplayName("토큰이 있으면 검증 후 AuthenticationManager 에 인증을 위임한다.")
  @Test
  public void attemptAuthenticationWithTokenTest() throws IOException, ServletException {
    // given
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    JwtAuthenticationToken authResult =
        JwtAuthenticationToken.of(1L, TOKEN, List.of(new SimpleGrantedAuthority("ROLE_BASIC")));

    given(tokenResolver.resolveAccessTokenOrNull(request)).willReturn(TOKEN);
    given(authenticationManager.authenticate(any(Authentication.class))).willReturn(authResult);

    // when
    Authentication authentication =
        jwtAuthenticationFilter.attemptAuthentication(request, response);

    // then
    assertThat(authentication).isSameAs(authResult);
    then(jwtTokenUtil).should().validate(TOKEN);

    ArgumentCaptor<Authentication> authRequestCaptor =
        ArgumentCaptor.forClass(Authentication.class);
    then(authenticationManager).should().authenticate(authRequestCaptor.capture());
    assertThat(authRequestCaptor.getValue()).isInstanceOf(JwtAuthenticationToken.class);
    assertThat(authRequestCaptor.getValue().getPrincipal()).isEqualTo(TOKEN);
  }

  @DisplayName("토큰이 없으면 ROLE_ANONYMOUS 권한의 익명 인증을 반환한다.")
  @Test
  public void attemptAuthenticationWithoutTokenTest() throws IOException, ServletException {
    // given
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    given(tokenResolver.resolveAccessTokenOrNull(request)).willReturn(null);

    // when
    Authentication authentication =
        jwtAuthenticationFilter.attemptAuthentication(request, response);

    // then
    assertThat(authentication).isInstanceOf(AnonymousAuthenticationToken.class);
    assertThat(authentication.getAuthorities())
        .extracting(GrantedAuthority::getAuthority)
        .containsExactly("ROLE_ANONYMOUS");
    then(authenticationManager).should(never()).authenticate(any());
  }

  @DisplayName("인증에 성공하면 SecurityContext 에 인증 정보를 저장하고 다음 필터를 호출한다.")
  @Test
  public void successfulAuthenticationTest() throws IOException, ServletException {
    // given
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    JwtAuthenticationToken authResult =
        JwtAuthenticationToken.of(1L, TOKEN, List.of(new SimpleGrantedAuthority("ROLE_BASIC")));

    given(tokenResolver.resolveAccessTokenOrNull(request)).willReturn(TOKEN);
    given(authenticationManager.authenticate(any(Authentication.class))).willReturn(authResult);

    // when
    jwtAuthenticationFilter.doFilter(request, response, filterChain);

    // then
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isSameAs(authResult);
    then(filterChain).should().doFilter(request, response);
  }

  @DisplayName("토큰이 없어도 익명 인증으로 다음 필터를 호출한다.")
  @Test
  public void anonymousAuthenticationPassesFilterTest() throws IOException, ServletException {
    // given
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    given(tokenResolver.resolveAccessTokenOrNull(request)).willReturn(null);

    // when
    jwtAuthenticationFilter.doFilter(request, response, filterChain);

    // then
    assertThat(SecurityContextHolder.getContext().getAuthentication())
        .isInstanceOf(AnonymousAuthenticationToken.class);
    then(filterChain).should().doFilter(request, response);
  }

  @DisplayName("유효하지 않은 토큰이면 401 상태와 JWT_INVALID 에러 응답을 내려주고 다음 필터를 호출하지 않는다.")
  @Test
  public void unsuccessfulAuthenticationTest() throws IOException, ServletException {
    // given
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();

    given(tokenResolver.resolveAccessTokenOrNull(request)).willReturn(TOKEN);
    willThrow(new InvalidTokenException()).given(jwtTokenUtil).validate(TOKEN);

    // when
    jwtAuthenticationFilter.doFilter(request, response, filterChain);

    // then
    assertThat(response.getStatus()).isEqualTo(401);
    assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);

    JsonNode body =
        new ObjectMapper().readTree(response.getContentAsString(StandardCharsets.UTF_8));
    assertThat(body.get("code").asText()).isEqualTo("A005");

    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    then(filterChain).should(never()).doFilter(any(), any());
  }
}
