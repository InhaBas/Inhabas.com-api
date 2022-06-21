package com.inhabas.api.config;

import com.inhabas.api.auth.domain.token.jwtUtils.JwtTokenProvider;
import com.inhabas.api.auth.domain.token.securityFilter.InvalidJwtTokenHandler;
import com.inhabas.api.auth.domain.token.securityFilter.TokenAuthenticationProcessingFilter;
import com.inhabas.api.auth.domain.token.securityFilter.UserPrincipalService;
import com.inhabas.api.domain.member.security.Hierarchical;
import com.inhabas.api.domain.member.type.wrapper.Role;
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

        private final JwtTokenProvider jwtTokenProvider;
        private final UserPrincipalService userPrincipalService;
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

                    .addFilterAfter(new TokenAuthenticationProcessingFilter(
                            jwtTokenProvider,
                            new InvalidJwtTokenHandler(),
                            userPrincipalService), LogoutFilter.class
                    )

                    .authorizeRequests()
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                        .antMatchers("/jwt/**").permitAll()
                        .antMatchers(HttpMethod.GET, "/menu/**", "/signUp/schedule", "/member/chief").permitAll()
                        .antMatchers("/signUp/**").hasRole(Role.ANONYMOUS.toString())
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

        private final JwtTokenProvider jwtTokenProvider;
        private final UserPrincipalService userPrincipalService;
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

                    .addFilterAfter(new TokenAuthenticationProcessingFilter(
                            jwtTokenProvider,
                            new InvalidJwtTokenHandler(),
                            userPrincipalService), LogoutFilter.class
                    )

                    .authorizeRequests()
                        .expressionHandler(expressionHandler())
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                        .antMatchers("/swagger", "/swagger-ui/**", "/docs/**", "/jwt/**").permitAll()
                        .antMatchers(HttpMethod.GET, "/menu/**", "/signUp/schedule", "/member/chief").permitAll()
                        .antMatchers("/signUp/**").hasRole(Role.ANONYMOUS.toString())
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
