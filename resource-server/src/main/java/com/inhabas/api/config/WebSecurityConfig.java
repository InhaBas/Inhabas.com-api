package com.inhabas.api.config;

import com.inhabas.api.auth.AuthBeansConfig;
import com.inhabas.api.auth.domain.token.CustomRequestMatcher;
import com.inhabas.api.auth.domain.token.securityFilter.JwtAuthenticationEntryPoint;
import com.inhabas.api.auth.domain.token.securityFilter.JwtAuthenticationFilter;
import com.inhabas.api.auth.domain.token.securityFilter.TokenAuthenticationProcessingFilter;
import com.inhabas.api.domain.member.security.Hierarchical;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.inhabas.api.domain.member.domain.valueObject.Role.*;

/**
 * api 엔드포인트에 대한 여러 보안 설정을 담당함. 인증 관련 보안 설정은 {@link com.inhabas.api.auth.AuthSecurityConfig AuthSecurityConfig} 참고
 */
public class WebSecurityConfig {

    private static final String[] AUTH_WHITELIST_SWAGGER = {"/swagger-ui/**", "/swagger/**", "/docs/**"};
    private static final String[] AUTH_WHITELIST_STATIC = {"/static/css/**", "/static/js/**", "*.ico"};

    @Order(1)
    @EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
    @EnableWebSecurity
    @RequiredArgsConstructor
    @Profile({"production"})
    public static class ApiSecurityForProduction extends WebSecurityConfigurerAdapter {

        private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
        private final Hierarchical hierarchy;
        private final AuthBeansConfig authBeansConfig;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .httpBasic().disable()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()

                    .csrf()
                    .disable()


                    .authorizeRequests()
                    .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                    // jwt 토큰
                    .antMatchers("/jwt/**").permitAll()
                    // 페이지 기본 정보(메뉴, 회원가입일정, 회장 연락처)
                    .antMatchers(HttpMethod.GET, "/menu/**", "/menus", "/signUp/schedule", "/member/chief").permitAll()
                    // 회원가입은 ANONYMOUS 권한은 명시적으로 부여받은 상태에서만 가능
                    .antMatchers("/signUp/**").hasRole(ANONYMOUS.toString())
                    // 회계내역
                    .antMatchers(HttpMethod.GET, "/budget/history/**", "/budget/histories", "/budget/application/**", "/budget/applications").hasRole(SECRETARY.toString())
                    .antMatchers("/budget/history/**").hasRole(SECRETARY.toString())
                    // 강의
                    .antMatchers("/lecture/**/status").hasRole(EXECUTIVES.toString())
                    .antMatchers("/lecture/**").hasRole(DEACTIVATED.toString())
                    // 그 외
                    .anyRequest().hasRole(DEACTIVATED.toString())

                    .expressionHandler(expressionHandler());

            http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        }

        @Bean
        public SecurityExpressionHandler<FilterInvocation> expressionHandler() {
            DefaultWebSecurityExpressionHandler webSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
            webSecurityExpressionHandler.setRoleHierarchy(hierarchy.getHierarchy());
            return webSecurityExpressionHandler;
        }

        @Bean
        public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
            final List<String> skipPaths = new ArrayList<>();
            skipPaths.addAll(Arrays.stream(AUTH_WHITELIST_SWAGGER).collect(Collectors.toList()));
            skipPaths.addAll(Arrays.stream(AUTH_WHITELIST_STATIC).collect(Collectors.toList()));

            final RequestMatcher requestMatcher = new CustomRequestMatcher(skipPaths);
            final JwtAuthenticationFilter filter = new JwtAuthenticationFilter(
                    requestMatcher,
                    authBeansConfig.tokenProvider(),
                    authBeansConfig.tokenResolver(),
                    authBeansConfig.userPrincipalService()
            );

            filter.setAuthenticationManager(super.authenticationManager());
            return filter;
        }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }
    }


    @Order(1)
    @EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
    @EnableWebSecurity
    @RequiredArgsConstructor
    @Profile({"local", "dev", "default_mvc_test"})
    public static class ApiSecurityForDev extends WebSecurityConfigurerAdapter {

        private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
        private final Hierarchical hierarchy;
        private final AuthBeansConfig authBeansConfig;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .httpBasic().disable()
                    .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .and()
                    .cors().and()
                    .csrf().disable()
                    .exceptionHandling()
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                    .and()

                    .authorizeRequests()
                        .expressionHandler(expressionHandler())
//                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                    // swagger 명세
                        .antMatchers("/swagger", "/swagger-ui/**", "/docs/**").permitAll()
                    // jwt 토큰
                        .antMatchers("/jwt/**").permitAll()
                    // 페이지 기본 정보(메뉴, 회원가입일정, 회장 연락처)
                        .antMatchers(HttpMethod.GET, "/menu/**", "/menus", "/signUp/schedule", "/member/chief", "/members/**").permitAll()
                    // 회원가입은 ANONYMOUS 권한은 명시적으로 부여받은 상태에서만 가능
                        .antMatchers("/signUp/**").hasRole(ANONYMOUS.toString())
                    // 회계내역
                        .antMatchers(HttpMethod.GET, "/budget/history/**", "/budget/histories", "/budget/application/**", "/budget/applications").hasRole(SECRETARY.toString())
                        .antMatchers("/budget/history/**").hasAuthority("Team_총무")
                    // 강의
                        .antMatchers("/lecture/**/status").hasRole(EXECUTIVES.toString())
                        .antMatchers("/lecture/**").hasRole(DEACTIVATED.toString())
                    // 그 외
                        .antMatchers("/error/**").hasRole(ANONYMOUS.toString())
                        .anyRequest().hasRole(BASIC.toString());

            http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        }

        @Bean
        public SecurityExpressionHandler<FilterInvocation> expressionHandler() {
            DefaultWebSecurityExpressionHandler webSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
            webSecurityExpressionHandler.setRoleHierarchy(hierarchy.getHierarchy());
            return webSecurityExpressionHandler;
        }

        @Bean
        public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
            final List<String> skipPaths = new ArrayList<>();
            skipPaths.addAll(Arrays.stream(AUTH_WHITELIST_SWAGGER).collect(Collectors.toList()));
            skipPaths.addAll(Arrays.stream(AUTH_WHITELIST_STATIC).collect(Collectors.toList()));

            final RequestMatcher requestMatcher = new CustomRequestMatcher(skipPaths);
            final JwtAuthenticationFilter filter = new JwtAuthenticationFilter(
                    requestMatcher,
                    authBeansConfig.tokenProvider(),
                    authBeansConfig.tokenResolver(),
                    authBeansConfig.userPrincipalService()
            );

            filter.setAuthenticationManager(super.authenticationManager());
            return filter;
        }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }
    }
}
