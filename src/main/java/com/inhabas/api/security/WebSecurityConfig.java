package com.inhabas.api.security;

import com.inhabas.api.security.domain.AuthUserService;
import com.inhabas.api.security.jwtUtils.JwtAuthenticationProcessingFilter;
import com.inhabas.api.security.jwtUtils.JwtTokenProvider;
import com.inhabas.api.security.oauth2.CustomAuthenticationFailureHandler;
import com.inhabas.api.security.oauth2.CustomAuthenticationSuccessHandler;
import com.inhabas.api.security.oauth2.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;


public class WebSecurityConfig {

    @Order(2)
    @EnableWebSecurity
    @Profile({"local", "dev", "production"})
    public static class OpenApi extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // 공개된 api, 접근 수준이 가장 낮다.
            http
                    .httpBasic().disable()
                    .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .and()
                    .csrf()
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .and()

                    .authorizeRequests()
                    .antMatchers("/login").permitAll();  // 개발 테스트용 임시 로그인 페이지
        }
    }


    @Order(0)
    @EnableWebSecurity
    @RequiredArgsConstructor
    @Profile({"local", "dev", "production"})
    @EnableConfigurationProperties(LoginUrlProperty.class)
    public static class OAuth2AuthenticationApi extends WebSecurityConfigurerAdapter {

        private final CustomOAuth2UserService customOAuth2UserService;
        private final LoginUrlProperty loginUrlProperty;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            /* 소셜 로그인 api
             *
             * 진행과정은 아래와 같다.
             *   - 1. 소셜로그인 진행
             *   - 2. 소셜 계정으로 인증이 완료되면 OAuth2LoginController 로 리다이렉트
             *       2-1. 기존회원이면 3으로 이동
             *       2-2. 신규회원이면 회원가입을 위한 리다이렉트, 회원가입 후 3으로 이동
             *   - 3. jwt 토큰 발급 및 로그인 처리
             *
             * 회원가입이나, jwt 토큰 발급을 위한 url 로 함부로 접근할 수 없게 하기 위해
             * jwt 토근이 발급되기 이전까지는 OAuth2 인증 결과를 세션을 통해서 유지함.
             * 따라서 critical 한 url 에 대해서 OAuth2 인증이 완료된 세션에 한해서만 허용.
             * */
            http
                    .antMatcher("/login/**")
                    .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                        .and()
                    .csrf()
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .and()
                    .oauth2Login(oauth2Login ->
                            oauth2Login
                                    .failureHandler(new CustomAuthenticationFailureHandler(loginUrlProperty.getFailureUrl()))
                                    .successHandler(new CustomAuthenticationSuccessHandler(loginUrlProperty.getSuccessUrl()))
                                    .userInfoEndpoint().userService(customOAuth2UserService).and()
                                    .authorizationEndpoint().baseUri("/login/oauth2/authorization"))
                    .authorizeRequests(request ->
                            request.antMatchers(
                                        loginUrlProperty.getSuccessUrl(),
                                        loginUrlProperty.getFailureUrl()).hasRole("USER")
                                    .anyRequest().permitAll()
                    );

        }
    }

    @Order(1)
    @EnableWebSecurity
    @RequiredArgsConstructor
    @Profile({"local", "dev", "production"})
    public static class JwtAuthenticationApi extends WebSecurityConfigurerAdapter {

        private final JwtTokenProvider jwtTokenProvider;
        private final AuthUserService authUserService;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .and()

                    .csrf()
                        .disable()

                    .addFilterAfter(new JwtAuthenticationProcessingFilter(authUserService, jwtTokenProvider), LogoutFilter.class)

                    .authorizeRequests()
                    .anyRequest().hasRole("MEMBER");
        }
    }

}
