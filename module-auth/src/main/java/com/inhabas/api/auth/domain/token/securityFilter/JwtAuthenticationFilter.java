package com.inhabas.api.auth.domain.token.securityFilter;

import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role.ANONYMOUS;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inhabas.api.auth.domain.error.ErrorCode;
import com.inhabas.api.auth.domain.error.ErrorResponse;
import com.inhabas.api.auth.domain.error.authException.CustomAuthException;
import com.inhabas.api.auth.domain.token.TokenResolver;
import com.inhabas.api.auth.domain.token.jwtUtils.JwtAuthenticationToken;
import com.inhabas.api.auth.domain.token.jwtUtils.JwtTokenUtil;

@Slf4j
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  private final JwtTokenUtil jwtTokenUtil;
  private final TokenResolver tokenResolver;
  private static final String ROLE_PREFIX = "ROLE_";

  public JwtAuthenticationFilter(
      RequestMatcher requestMatcher, JwtTokenUtil jwtTokenUtil, TokenResolver tokenResolver) {

    super(requestMatcher); // only work for requests with this pattern
    this.jwtTokenUtil = jwtTokenUtil;
    this.tokenResolver = tokenResolver;
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException, IOException {

    final String token = tokenResolver.resolveAccessTokenOrNull(request);

    if (token != null) {
      jwtTokenUtil.validate(token);
      final JwtAuthenticationToken authRequest = JwtAuthenticationToken.of(token);
      return this.getAuthenticationManager().authenticate(authRequest);
    } else {
      log.info("Anonymous user request");
      return new AnonymousAuthenticationToken(
          "anonymous",
          this.hashCode(),
          Collections.singleton(new SimpleGrantedAuthority(ROLE_PREFIX + ANONYMOUS)));
    }
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult)
      throws IOException, ServletException {
    SecurityContextHolder.getContext().setAuthentication(authResult);
    log.debug("jwt token authentication success!");

    chain.doFilter(request, response);
  }

  @Override
  protected void unsuccessfulAuthentication(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
      throws IOException {
    SecurityContextHolder.clearContext();
    log.info("Failed to process authentication request: {}", failed.getMessage());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    if (failed instanceof CustomAuthException) {
      response.setStatus(((CustomAuthException) failed).getErrorCode().getStatus());
      try (OutputStream os = response.getOutputStream()) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(
            os, ErrorResponse.of(((CustomAuthException) failed).getErrorCode()));
        os.flush();
      }
      return;
    }

    // 일반 인증 실패에 대해서는 401로 응답하고 자세한 정보를 노출하지 않음
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    try (OutputStream os = response.getOutputStream()) {
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.writeValue(os, ErrorResponse.of(ErrorCode.JWT_INVALID));
      os.flush();
    }
  }
}
