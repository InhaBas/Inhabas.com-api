package com.inhabas.api.auth.config;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsUtils;

import com.inhabas.api.auth.domain.oauth2.CustomOAuth2UserService;
import com.inhabas.api.auth.domain.oauth2.cookie.HttpCookieOAuth2AuthorizationRequestRepository;
import com.inhabas.api.auth.domain.oauth2.handler.Oauth2AuthenticationFailureHandler;
import com.inhabas.api.auth.domain.oauth2.handler.Oauth2AuthenticationSuccessHandler;

@Order(0) // 인증 관련 security filter chain 은 우선순위가 가장 높아야 함.
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@Profile({"dev1", "dev2", "local", "prod1", "prod2"}) // 테스트에는 포함시키지 않음.
public class AuthSecurityConfig {

  private final CustomOAuth2UserService customOAuth2UserService;
  private final OAuth2AuthorizedClientService authorizedClientService;
  private final Oauth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;
  private final Oauth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler;
  private final HttpCookieOAuth2AuthorizationRequestRepository
      httpCookieOAuth2AuthorizationRequestRepository;

  @Bean
  @Order(0)
  public SecurityFilterChain authSecurityFilterChain(HttpSecurity http) throws Exception {

    http
        // /login/** 경로에만 이 보안 체인 적용
        .requestMatcher(new AntPathRequestMatcher("/login/**"))
        // 세션 생성 금지
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .cors(cors -> {})
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            authorize ->
                authorize
                    .requestMatchers(request -> CorsUtils.isPreFlightRequest(request))
                    .permitAll()
                    .anyRequest()
                    .permitAll())
        // Oauth 로그인 설정
        .oauth2Login(
            oauth2 ->
                oauth2
                    .authorizedClientService(authorizedClientService)
                    .authorizationEndpoint(
                        authorization ->
                            authorization
                                .baseUri("/login/oauth2/authorization")
                                .authorizationRequestRepository(
                                    httpCookieOAuth2AuthorizationRequestRepository))
                    .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                    .failureHandler(oauth2AuthenticationFailureHandler)
                    .successHandler(oauth2AuthenticationSuccessHandler));

    return http.build();
  }
}
