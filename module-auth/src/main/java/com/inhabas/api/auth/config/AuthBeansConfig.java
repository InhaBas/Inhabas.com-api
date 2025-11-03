package com.inhabas.api.auth.config;

import javax.sql.DataSource;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.client.JdbcOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

import com.inhabas.api.auth.domain.oauth2.cookie.HttpCookieOAuth2AuthorizationRequestRepository;
import com.inhabas.api.auth.domain.oauth2.handler.Oauth2AuthenticationFailureHandler;
import com.inhabas.api.auth.domain.oauth2.handler.Oauth2AuthenticationSuccessHandler;
import com.inhabas.api.auth.domain.oauth2.userAuthorityProvider.DefaultUserAuthorityProvider;
import com.inhabas.api.auth.domain.oauth2.userAuthorityProvider.UserAuthorityProvider;
import com.inhabas.api.auth.domain.token.TokenReIssuer;
import com.inhabas.api.auth.domain.token.TokenResolver;
import com.inhabas.api.auth.domain.token.TokenUtil;
import com.inhabas.api.auth.domain.token.jwtUtils.JwtTokenReIssuer;
import com.inhabas.api.auth.domain.token.jwtUtils.JwtTokenResolver;
import com.inhabas.api.auth.domain.token.jwtUtils.JwtTokenUtil;
import com.inhabas.api.auth.domain.token.jwtUtils.refreshToken.RefreshTokenRepository;
import com.inhabas.api.auth.domain.token.securityFilter.DefaultUserPrincipalService;
import com.inhabas.api.auth.domain.token.securityFilter.UserPrincipalService;

@Configuration
@RequiredArgsConstructor
public class AuthBeansConfig {

  private final JwtTokenUtil jwtTokenUtil;
  private final AuthProperties authProperties;
  private final RefreshTokenRepository refreshTokenRepository;

  @Bean
  public ApplicationRunner jwtSecretKeyStrengthChecker(JwtTokenUtil jwtTokenUtil) {
    return args -> jwtTokenUtil.validateSecretKeyStrength();
  }

  @Bean
  public HttpCookieOAuth2AuthorizationRequestRepository
      httpCookieOAuth2AuthorizationRequestRepository() {
    return new HttpCookieOAuth2AuthorizationRequestRepository();
  }

  @Bean
  public Oauth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler(
      RefreshTokenRepository refreshTokenRepository) {
    return new Oauth2AuthenticationSuccessHandler(
        jwtTokenUtil, this.authProperties, this.httpCookieOAuth2AuthorizationRequestRepository());
  }

  @Bean
  public Oauth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler() {
    return new Oauth2AuthenticationFailureHandler(
        this.authProperties, this.httpCookieOAuth2AuthorizationRequestRepository());
  }

  @ConditionalOnMissingBean
  public UserAuthorityProvider userAuthorityService() {
    return new DefaultUserAuthorityProvider();
  }

  @Bean
  public OAuth2AuthorizedClientService authorizedClientService(
      DataSource dataSource, ClientRegistrationRepository clientRegistrationRepository) {
    return new JdbcOAuth2AuthorizedClientService(
        new JdbcTemplate(dataSource), clientRegistrationRepository);
  }

  @ConditionalOnMissingBean
  public UserPrincipalService userPrincipalService() {
    return new DefaultUserPrincipalService();
  }

  @Bean
  public TokenResolver tokenResolver() {
    return new JwtTokenResolver();
  }

  @Bean
  public TokenReIssuer tokenReIssuer(
      TokenUtil tokenUtil,
      TokenResolver tokenResolver,
      RefreshTokenRepository refreshTokenRepository) {
    return new JwtTokenReIssuer(tokenUtil, tokenResolver, refreshTokenRepository);
  }
}
