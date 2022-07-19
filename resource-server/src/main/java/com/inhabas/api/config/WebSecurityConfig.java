package com.inhabas.api.config;

import com.inhabas.api.auth.domain.token.securityFilter.TokenAuthenticationProcessingFilter;
import com.inhabas.api.domain.member.domain.valueObject.Role;
import com.inhabas.api.domain.member.security.Hierarchical;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsUtils;

/**
 * api 엔드포인트에 대한 여러 보안 설정을 담당함. 인증 관련 보안 설정은 {@link com.inhabas.api.auth.AuthSecurityConfig AuthSecurityConfig} 참고
 */
public class WebSecurityConfig {

    @Order(1)
    @EnableWebSecurity
    @RequiredArgsConstructor
    @Profile({"production"})
    public static class ApiSecurityForProduction extends WebSecurityConfigurerAdapter {

        private final TokenAuthenticationProcessingFilter tokenAuthenticationProcessingFilter;
        private final Hierarchical hierarchy;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .httpBasic().disable()
                    .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .and()

                    .csrf()
                        .disable()

                    .addFilterAfter(tokenAuthenticationProcessingFilter, LogoutFilter.class
                    )

                    .authorizeRequests()
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                    // jwt 토큰
                        .antMatchers("/jwt/**").permitAll()
                    // 페이지 기본 정보(메뉴, 회원가입일정, 회장 연락처)
                        .antMatchers(HttpMethod.GET, "/menu/**", "/menus", "/signUp/schedule", "/member/chief").permitAll()
                    // 회원가입은 ANONYMOUS 권한은 명시적으로 부여받은 상태에서만 가능
                        .antMatchers("/signUp/**").hasRole(Role.ANONYMOUS.toString())
                    // 회계내역
                        .antMatchers(HttpMethod.GET, "/budget/history/**", "/budget/histories", "/budget/application/**", "/budget/applications").permitAll()
                        .antMatchers("/budget/history/**").hasAuthority("Team_총무")
                    // 그 외
                        .anyRequest().hasRole(Role.BASIC_MEMBER.toString())

                    .expressionHandler(expressionHandler());
        }

        @Bean
        public SecurityExpressionHandler<FilterInvocation> expressionHandler() {
            DefaultWebSecurityExpressionHandler webSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
            webSecurityExpressionHandler.setRoleHierarchy(hierarchy.getHierarchy());
            return webSecurityExpressionHandler;
        }
    }


    @Order(1)
    @EnableWebSecurity
    @RequiredArgsConstructor
    @Profile({"local", "dev", "default_mvc_test"})
    public static class ApiSecurityForDev extends WebSecurityConfigurerAdapter {

        private final TokenAuthenticationProcessingFilter tokenAuthenticationProcessingFilter;
        private final Hierarchical hierarchy;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .httpBasic().disable()
                    .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .and()
                    .cors().and()
                    .csrf().disable()

                    .addFilterAfter(tokenAuthenticationProcessingFilter, LogoutFilter.class)

                    .authorizeRequests()
                        .expressionHandler(expressionHandler())
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                    // swagger 명세
                        .antMatchers("/swagger", "/swagger-ui/**", "/docs/**").permitAll()
                    // jwt 토큰
                        .antMatchers("/jwt/**").permitAll()
                    // 페이지 기본 정보(메뉴, 회원가입일정, 회장 연락처)
                        .antMatchers(HttpMethod.GET, "/menu/**", "/menus", "/signUp/schedule", "/member/chief").permitAll()
                    // 회원가입은 ANONYMOUS 권한은 명시적으로 부여받은 상태에서만 가능
                        .antMatchers("/signUp/**").hasRole(Role.ANONYMOUS.toString())
                    // 회계내역
                        .antMatchers(HttpMethod.GET, "/budget/history/**", "/budget/histories", "/budget/application/**", "/budget/applications").permitAll()
                        .antMatchers("/budget/history/**").hasAuthority("Team_총무")
                    // 그 외
                        .anyRequest().hasRole(Role.BASIC_MEMBER.toString());
        }

        @Bean
        public SecurityExpressionHandler<FilterInvocation> expressionHandler() {
            DefaultWebSecurityExpressionHandler webSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
            webSecurityExpressionHandler.setRoleHierarchy(hierarchy.getHierarchy());
            return webSecurityExpressionHandler;
        }
    }
}
