package com.inhabas.api.security;

import com.inhabas.api.domain.member.type.wrapper.Role;
import com.inhabas.api.security.domain.AuthUserService;
import com.inhabas.api.security.jwtUtils.InvalidJwtTokenHandler;
import com.inhabas.api.security.jwtUtils.JwtAuthenticationProcessingFilter;
import com.inhabas.api.security.jwtUtils.JwtTokenProvider;
import com.inhabas.api.security.oauth2.CustomAuthenticationFailureHandler;
import com.inhabas.api.security.oauth2.CustomAuthenticationSuccessHandler;
import com.inhabas.api.security.oauth2.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebSecurityConfig_dev {

    @Order(0)
    @EnableWebSecurity
    @RequiredArgsConstructor
    @Profile({"local", "dev"})
    public static class OAuth2AuthenticationApi extends WebSecurityConfigurerAdapter {

        private final CustomOAuth2UserService customOAuth2UserService;
        private final AuthenticateEndPointUrlProperties authenticateEndPointUrlProperties;

        /** 소셜 로그인 api <br><br>
         *
         * 진행과정은 아래와 같다.<br>
         * <ol>
         *      <li>소셜로그인 진행</li>
         *      <li>소셜 계정으로 인증이 완료되면 OAuth2LoginController 로 리다이렉트</li>
         *      <ol style="list-style-type:lower-alpha">
         *          <li>기존회원이면 3으로 이동</li>
         *          <li>신규회원이면 회원가입을 위한 리다이렉트, 회원가입 후 3으로 이동</li>
         *      </ol>
         *      <li>jwt 토큰 발급 및 로그인 처리</li>
         * </ol>
         *
         * 회원가입이나, jwt 토큰 발급을 위한 url 로 함부로 접근할 수 없게 하기 위해
         * jwt 토근이 발급되기 이전까지는 OAuth2 인증 결과를 세션을 통해서 유지함.
         * 따라서 critical 한 url 에 대해서 OAuth2 인증이 완료된 세션에 한해서만 허용.
         */
        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http
                    .antMatcher("/login/**")
                    .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                        .and()
                    .csrf()
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .and()
                    .cors().and()
                    .oauth2Login(oauth2Login ->
                            oauth2Login
                                    .failureHandler(new CustomAuthenticationFailureHandler(authenticateEndPointUrlProperties.getOauth2FailureHandleUrl()))
                                    .successHandler(new CustomAuthenticationSuccessHandler(authenticateEndPointUrlProperties.getOauth2SuccessHandleUrl()))
                                    .userInfoEndpoint().userService(customOAuth2UserService).and()
                                    .authorizationEndpoint().baseUri("/login/oauth2/authorization"))
                    .authorizeRequests(request ->
                            request.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                                    .antMatchers(
                                            authenticateEndPointUrlProperties.getOauth2SuccessHandleUrl(),
                                            authenticateEndPointUrlProperties.getOauth2FailureHandleUrl()).hasRole("USER")
                                    .anyRequest().permitAll()
                    );
        }


    }

    @Order(1)
    @EnableWebSecurity
    @RequiredArgsConstructor
    @Profile({"local", "dev"})
    public static class JwtAuthenticationApi extends WebSecurityConfigurerAdapter {

        private final JwtTokenProvider jwtTokenProvider;
        private final AuthUserService authUserService;
        private final AuthenticateEndPointUrlProperties authenticateEndPointUrlProperties;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .httpBasic().disable()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .cors().and()
                    .csrf().disable()

                    .addFilterAfter(new JwtAuthenticationProcessingFilter(
                            authUserService,
                            jwtTokenProvider,
                            new InvalidJwtTokenHandler()), LogoutFilter.class
                    )

                    .authorizeRequests()
                    .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                    .antMatchers("/swagger", "/swagger-ui/**", "/docs/**", "/jwt/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/menu/**").permitAll()
                    .antMatchers("/signUp/**").hasRole(Role.ANONYMOUS.toString())
                    .anyRequest().hasRole(Role.BASIC_MEMBER.toString());
        }

    }
}

