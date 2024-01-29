package com.inhabas.api.auth;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.cors.CorsUtils;

import com.inhabas.api.auth.domain.oauth2.CustomOAuth2UserService;
import com.inhabas.api.auth.domain.oauth2.cookie.HttpCookieOAuth2AuthorizationRequestRepository;
import com.inhabas.api.auth.domain.oauth2.handler.Oauth2AuthenticationFailureHandler;
import com.inhabas.api.auth.domain.oauth2.handler.Oauth2AuthenticationSuccessHandler;

@Order(0) // 인증 관련 security filter chain 은 우선순위가 가장 높아야 함.
@EnableWebSecurity
@RequiredArgsConstructor
@Profile({"dev", "local", "production"}) // 테스트에는 포함시키지 않음.
public class AuthSecurityConfig extends WebSecurityConfigurerAdapter {

  private final CustomOAuth2UserService customOAuth2UserService;
  private final OAuth2AuthorizedClientService authorizedClientService;
  private final Oauth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;
  private final Oauth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler;
  private final HttpCookieOAuth2AuthorizationRequestRepository
      httpCookieOAuth2AuthorizationRequestRepository;

  /**
   * 소셜 로그인 api <br>
   * <br>
   * 진행과정은 아래와 같다.<br>
   *
   * <ol>
   *   <li>사용자가 소셜로그인 시작. (프론트에서 redirect_url 보내줘야함.)
   *   <li>OAuth2 인증 진행 -> 기존 회원인지 검사
   *       <ol style="list-style-type:lower-alpha">
   *         <li>성공 -> OAuth2AuthenticationSuccessHandler
   *             <ol>
   *               <li>프론트에서 보내준 redirect_url 검증 (-> 실패하면 failure handler 에서 처리)
   *               <li>jwt 토큰 발급 및 로그인 처리
   *               <li>리다이렉트
   *             </ol>
   *         <li>실패 -> OAuth2AuthenticationFailureHandler
   *       </ol>
   * </ol>
   *
   * 회원가입이나, jwt 토큰 발급을 위한 url 로 함부로 접근할 수 없게 하기 위해 jwt 토근이 발급되기 이전까지는 OAuth2 인증 결과를 세션을 통해서 유지함.
   * 따라서 critical 한 url 에 대해서 OAuth2 인증이 완료된 세션에 한해서만 허용.
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.requestMatchers()
        .antMatchers("/login/**")
        .and()
        // 세션 생성 금지
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .cors()
        .and()
        .authorizeRequests()
        .requestMatchers(CorsUtils::isPreFlightRequest)
        .permitAll()
        .anyRequest()
        .permitAll()
        .and()
        .csrf()
        .disable()

        // Oauth 로그인 설정
        .oauth2Login()
        .authorizedClientService(authorizedClientService)
        .authorizationEndpoint()
        .baseUri("/login/oauth2/authorization")
        .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository)
        .and()

        // 사용자 정보를 가져오는 엔드포인트에 대한 설정
        .userInfoEndpoint()
        .userService(customOAuth2UserService)
        .and()
        .failureHandler(oauth2AuthenticationFailureHandler)
        .successHandler(oauth2AuthenticationSuccessHandler);
  }
}
