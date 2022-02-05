package com.inhabas.api.security;

import com.inhabas.api.security.oauth2.CustomAuthenticationFailureHandler;
import com.inhabas.api.security.oauth2.CustomAuthenticationSuccessHandler;
import com.inhabas.api.security.oauth2.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@RequiredArgsConstructor
@EnableWebSecurity
@EnableConfigurationProperties(LoginUrlProperty.class)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final LoginUrlProperty loginUrlProperty;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable();

        // 공개된 api, 접근 수준이 가장 낮다.
        http
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .csrf()
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .and()

                .authorizeRequests()
                    .antMatchers("/login").permitAll();  // 개발 테스트용 임시 로그인 페이지

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
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                    .and()
                .csrf()
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .and()
                .oauth2Login()
                    .successHandler(new CustomAuthenticationSuccessHandler(loginUrlProperty.getSuccessUrl()))
                    .failureHandler(new CustomAuthenticationFailureHandler(loginUrlProperty.getFailureUrl()))
                    .userInfoEndpoint() // oauth2Login 성공 이후의 설정을 시작
                        .userService(customOAuth2UserService) // customOAuth2UserService에서 처리하겠다.
                    .and()

                .and()
                    .authorizeRequests()
                    .antMatchers(loginUrlProperty.getSuccessUrl()).hasRole("USER")  // 소셜로그인 인증 완료 후에 접근 가능
                    .antMatchers(loginUrlProperty.getFailureUrl()).hasRole("USER")  // 소셜로그인 인증 완료 후에 접근 가능
                    .anyRequest().authenticated();
    }

}
