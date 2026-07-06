package com.inhabas.testConfig;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.inhabas.api.auth.config.AuthBeansConfig;
import com.inhabas.api.auth.domain.token.CustomRequestMatcher;
import com.inhabas.api.auth.domain.token.jwtUtils.JwtTokenUtil;
import com.inhabas.api.auth.domain.token.securityFilter.JwtAuthenticationFilter;

@TestConfiguration
public class TestConfigurationForSecurity {

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter(
      JwtTokenUtil jwtTokenUtil,
      AuthBeansConfig authBeansConfig,
      AuthenticationManager authenticationManager)
      throws Exception {
    final List<RequestMatcher> skipPaths = new ArrayList<>();
    skipPaths.add(new CustomRequestMatcher("/**", "GET"));
    skipPaths.add(new CustomRequestMatcher("/**", "POST"));
    skipPaths.add(new CustomRequestMatcher("/**", "DELETE"));
    skipPaths.add(new CustomRequestMatcher("/**", "PUT"));
    final RequestMatcher requestMatcher = new AndRequestMatcher(skipPaths);
    final JwtAuthenticationFilter filter =
        new JwtAuthenticationFilter(requestMatcher, jwtTokenUtil, authBeansConfig.tokenResolver());
    filter.setAuthenticationManager(authenticationManager);
    return filter;
  }
}
